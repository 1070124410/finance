package com.finance.anubis.core.task.executor;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.finance.anubis.config.OffLineHttpResourceConfig;
import com.finance.anubis.constants.Constants;
import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.core.util.ExternalSortUtil;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.core.util.OSSUtil;
import com.finance.anubis.enums.OffLineResourceType;
import com.finance.anubis.response.Result;
import lombok.CustomLog;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/03/06 11:03
 * @Description 通过HTTP获取对账数据
 **/
@Component
public class HttpFetchExecutor extends PrepareDataTemplate {

    private final FileUtil fileUtil;

    private final RestTemplate restTemplate;

    private final Integer batchSize = 10000;

    private static Integer pageNo = 1;

    private final Integer pageSize = 50;

    public static final TypeReference<Result<JSON>> JSON_TYPE_REFERENCE = new TypeReference<Result<JSON>>() {
    };

    public HttpFetchExecutor(OSSUtil ossUtil, ExternalSortUtil fileSortUtil, FileUtil fileUtil, FileUtil fileUtil1, RestTemplate restTemplate) {
        super(OffLineResourceType.OffLineHttpResource, ossUtil, fileSortUtil, fileUtil);
        this.fileUtil = fileUtil1;
        this.restTemplate = restTemplate;
    }

    @Override
    public void prepareData(OffLineTaskActivity taskActivity, String key) {
        //任务配置
        OffLineHttpResourceConfig resourceConfig = (OffLineHttpResourceConfig) taskActivity.getResouceConfig(key);
        //任务上下文数据
        OffLineActivityContext.SourceContext sourceContext = taskActivity.getSourceContext(key);
        //准备文件头
        LinkedHashMap<String, String> mapping = resourceConfig.getJsonFormatMapping();
        sourceContext.setFileHeader(new ArrayList<>(mapping.keySet()));
        Comparator comparator = taskActivity.getComparator(key);
        //对账小文件路径集合
        List<String> pathList = new ArrayList<>();
        //小文件后缀(数字尾巴依次递增)
        int fileCount = 1;
        //内存临时存放数据
        List<String> dataList = new ArrayList<>();
        //准备请求对账数据参数
        String url = resourceConfig.getUrl();
        Method method = Method.valueOf(resourceConfig.getMethod());
        Map<String, Object> requestParams = sourceContext.getRequestParamMapping();
        requestParams.put(Constants.PAGESIZE, pageSize);
        StringBuilder getUrlBuilder = fileUtil.buildHttpUrl(url, requestParams);

        String body = null;
        do {
            requestParams.put(Constants.PAGENO, pageNo++);
            switch (method) {
                case GET:
                    StringBuilder builder = getUrlBuilder.append("&").append(Constants.PAGENO).append("={").append(pageNo).append("}");
                    body = restTemplate.getForObject(builder.toString(), String.class, requestParams);
                    break;
                case POST:
                    body = restTemplate.postForObject(url, requestParams, String.class, requestParams);
                    break;
            }
            JSON responseJson = JSONUtil.parse(body);

            Result<JSON> result = responseJson.toBean(JSON_TYPE_REFERENCE);
            if (result.isSuccess()) {
                List<JSON> data = result.getData().toBean(List.class);
                //如果数据为空则说明数据拉取完毕
                if (data.isEmpty()) {
                    break;
                }
                dataList.addAll(data.stream().map(json -> dealWithLine(json, mapping)).collect(Collectors.toList()));
                if (dataList.size() >= batchSize) {
                    List<String> collect = (List<String>) dataList.stream().sorted(comparator).collect(Collectors.toList());
                    dataList.clear();
                    String filePath = taskActivity.getAbsoluteFilePath().append(key).append(Constants.LocalFile.PART_RECONCILIATION_FILE).append(fileCount++).toString();
                    //todo 异步写
                    fileUtil.writeToLocal(collect, filePath);
                    pathList.add(filePath);
                    collect = null;
                }
            }
        } while (true);

        sourceContext.setPathList(pathList);
    }

    /**
     * 从每条上游数据中取出对账数据
     * @param json
     * @param mapping
     * @return
     */
    private String dealWithLine(JSON json, Map<String, String> mapping) {
        StringBuilder builder = new StringBuilder();
        List<String> values = (List<String>) mapping.values();
        for (int i = 0; i < values.size(); i++) {
            builder.append(json.getByPath(values.get(i)));
            if (i < values.size() - 1) {
                builder.append(Constants.OffLineTask.LOCAL_FILE_SPLIT);
            }
        }
        return builder.toString();
    }

}
