package com.finance.anubis.core.task.executor;

import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.FileType;
import com.finance.anubis.core.constants.enums.OffLineResourceType;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.util.ExternalSortUtil;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.exception.ErrorMsg;
import com.guming.api.json.JsonUtil;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import com.finance.anubis.core.config.OffLineFileResourceConfig;
import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.core.factory.FileParserUtilFactory;
import com.finance.anubis.core.util.OSSUtil;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/03/06 11:03
 * @Description 通过文件获取对账数据
 **/
@CustomLog
@Component
public class FileFetchExecutor extends PrepareDataTemplate {

    private final FileUtil fileUtil;

    private final Integer batchSize = 10000;

    private final FileParserUtilFactory fileParserUtilFactory;


    public FileFetchExecutor(OSSUtil ossUtil, ExternalSortUtil fileSortUtil, FileUtil fileUtil, FileParserUtilFactory fileParserUtilFactory) {
        super(OffLineResourceType.OffLineFileResource, ossUtil, fileSortUtil, fileUtil);
        this.fileUtil = fileUtil;
        this.fileParserUtilFactory = fileParserUtilFactory;
    }

    @Override
    public void prepareData(OffLineTaskActivity taskActivity, String key) {
        //任务配置
        OffLineFileResourceConfig resourceConfig = (OffLineFileResourceConfig) taskActivity.getResouceConfig(key);
        //任务上下文数据
        OffLineActivityContext.FileSourceContext sourceContext = (OffLineActivityContext.FileSourceContext) taskActivity.getSourceContext(key);
        //本地对账文件保存路径
        String fileName = taskActivity.getAbsoluteFilePath().append(key).append(Constants.LocalFile.ORIGIN_RECONCILIATION_FILE).toString();
        fileUtil.streamDownload(fileName, resourceConfig.getUrl(), HttpMethod.valueOf(resourceConfig.getMethod()), sourceContext.getRequestParamMapping());
        //对账小文件路径集合
        List<String> pathList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            //内存临时存放数据
            List<String> dataList = new ArrayList<>();
            //处理文件头(约定第一行必须是文件头)
            String line = reader.readLine();
            List<String> headers = Arrays.asList(line.split(resourceConfig.getFileFormatSplit()));
            sourceContext.setFileHeader(resourceConfig.getKeyList());
            //生成key和所在行位置下标的映射
            Map<String, Integer> keyLocMap = prepareKeyLocMap(resourceConfig, headers);
            Comparator comparator = taskActivity.getComparator(key);
            //小文件后缀(数字尾巴依次递增)
            int fileCount = 1;
            //正式开始读取对账数据
            line = reader.readLine();
            while (line != null) {
                if (StringUtils.isBlank(line)) {
                    line = reader.readLine();
                    continue;
                }
                //解析文件行内容
                FileType fileType = resourceConfig.getFileType();
                String parseLine = fileParserUtilFactory.getParseUtil(fileType).parseLine(line);
                String formatLine = dealWithLine(parseLine, resourceConfig.getKeyList(), keyLocMap);
                dataList.add(formatLine);
                //达到可写小文件阈值后先在内存排序然后写入小文件(后面可考虑异步写,注意读写速度问题)
                if (dataList.size() >= batchSize) {
                    List<String> tmpList = new ArrayList<>(dataList);
                    dataList.clear();
                    String path = taskActivity.getAbsoluteFilePath().append(key).append(Constants.LocalFile.PART_RECONCILIATION_FILE).append(fileCount++).toString();
                    saveBatchData(tmpList, comparator, path);
                    pathList.add(path);
                }
                line = reader.readLine();
            }
            //不够一个批次的单独写一份文件
            if (!dataList.isEmpty()) {
                String path = taskActivity.getAbsoluteFilePath().append(key).append(Constants.LocalFile.PART_RECONCILIATION_FILE).append(fileCount).toString();
                saveBatchData(dataList, comparator, path);
                pathList.add(path);
            }
            fileUtil.deleteLocalFiles(Collections.singletonList(fileName));
        } catch (IOException e) {
            log.error("FileFetchExecutor#prepareData error");
            ErrorMsg msg = new ErrorMsg(taskActivity.getBizKey(), key, taskActivity.getAction(), "FileFetchExecutor error", e);
            throw new StatusCodeException(Status.error(JsonUtil.toJson(msg)));
        }
        sourceContext.setPathList(pathList);
    }

    public Map<String, Integer> prepareKeyLocMap(OffLineFileResourceConfig config, List<String> fileHeader) {
        Map<String, Integer> keyLocMap = new HashMap<>();
        for (String key : config.getKeyList()) {
            keyLocMap.put(key, fileHeader.indexOf(key));
        }

        return keyLocMap;
    }

    public String dealWithLine(String line, List<String> keyList, Map<String, Integer> keyLocMap) {
        StringBuilder builder = new StringBuilder();
        String[] split = line.split(Constants.OffLineTask.LOCAL_FILE_SPLIT);
        for (int i = 0; i < keyList.size(); i++) {
            builder.append(split[keyLocMap.get(keyList.get(i))]);
            if (i < keyList.size() - 1) {
                builder.append(Constants.OffLineTask.LOCAL_FILE_SPLIT);
            }
        }

        return builder.toString();
    }

    public void saveBatchData(List<String> dataList, Comparator comparator, String path) {
        //sort
        List<String> collect = (List<String>) dataList.stream().sorted(comparator).collect(Collectors.toList());
        fileUtil.writeToLocal(collect, path);
        dataList.clear();
    }
}
