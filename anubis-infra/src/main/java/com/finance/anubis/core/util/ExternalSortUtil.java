package com.finance.anubis.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.util.math.InToPost;
import com.finance.anubis.core.util.math.ParsePost;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.finance.anubis.core.constants.Constants.OffLineTask.LOCAL_FILE_SPLIT;


@Component
@CustomLog
public class ExternalSortUtil {

    /**
     * 排序，把输入的多个有序的文件合并为一个有序文件
     *
     * @param pathList 若干个有序文件的路径
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends Comparable<T>> String doMerge(List<String> pathList, String prefix, Comparator comparator, OffLineTaskActivity activity, String key) throws IOException {
        try {
            //空数据直接返回空的新文件
            if (CollectionUtil.isEmpty(pathList)) {
                String tmpFile = prefix + System.currentTimeMillis();
                new File(tmpFile).createNewFile();
                return tmpFile;
            }
            int pathSize = pathList.size();
            if (pathSize == 1) {
                countSingleFileTotal(pathList.get(0), activity, key);
                return pathList.get(0);
            }
            if (pathSize == 2) {
                // 合并这两个文件
                return merge(pathList.get(0), pathList.get(1), prefix, comparator, activity, key);
            }
            // 如果pathList长度大于2，则分割为若干个两个文件路径
            List<String> newPath = new ArrayList<>();
            int i;
            for (i = 0; i < pathSize - 1; i += 2) {
                List<String> tmp = new ArrayList<>();
                tmp.add(pathList.get(i));
                tmp.add(pathList.get(i + 1));
                newPath.add(doMerge(tmp, prefix, comparator, activity, key));
            }
            if (i == pathSize - 1) {
                newPath.add(pathList.get(i));
            }
            return doMerge(newPath, prefix, comparator, activity, key);
        } catch (IOException e) {
            log.error("ExternalSortUtil#doMerge error,pathList:{}", pathList);
            throw e;
        }
    }

    /**
     * 合并两个有序文件,同时遍历行时计算总账
     *
     * @param file1
     * @param file2
     * @param prefix
     * @param comparator
     * @param activity
     * @param key
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends Comparable<T>> String merge(String file1, String file2, String prefix,
                                                  Comparator comparator, OffLineTaskActivity activity, String key) throws IOException {
        String tmpFile = prefix + System.currentTimeMillis();
        File f1 = new File(file1);
        File f2 = new File(file2);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmpFile));
        BufferedReader bufferedReader1 = new BufferedReader(new FileReader(f1));
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(f2));
        String line1 = bufferedReader1.readLine();
        String line2 = bufferedReader2.readLine();
        //计算总账所需参数
        String exp = activity.getResouceConfig(key).getComputeExpressions();
        List<String> totalKeys = activity.getResouceConfig(key).getCompareTotalKeys();
        List<String> fileHeader = activity.getSourceContext(key).getFileHeader();
        //临时存放总账数据
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);

        while (line1 != null && line2 != null) {
            int compare = comparator.compare(line1, line2);
            if (compare < 0) {
                bufferedWriter.write(line1 + "\n");
                totalAmount = totalAmount.add(countTotal(new InToPost(exp), line1, fileHeader, totalKeys));
                line1 = bufferedReader1.readLine();
            } else {
                bufferedWriter.write(line2 + "\n");
                totalAmount = totalAmount.add(countTotal(new InToPost(exp), line2, fileHeader, totalKeys));
                line2 = bufferedReader2.readLine();
            }
        }
        while (line1 != null) {
            bufferedWriter.write(line1 + "\n");
            totalAmount = totalAmount.add(countTotal(new InToPost(exp), line1, fileHeader, totalKeys));
            line1 = bufferedReader1.readLine();
        }
        while (line2 != null) {
            bufferedWriter.write(line2 + "\n");
            totalAmount = totalAmount.add(countTotal(new InToPost(exp), line2, fileHeader, totalKeys));
            line2 = bufferedReader2.readLine();
        }
        bufferedReader1.close();
        bufferedReader2.close();
        bufferedWriter.close();
        f1.delete();
        f2.delete();
        activity.getSourceContext(key).setTotalAmount(totalAmount);
        return tmpFile;
    }

    /**
     * 单个文件计算总账
     *
     * @param filePath
     * @param activity
     * @param key
     */
    public void countSingleFileTotal(String filePath, OffLineTaskActivity activity, String key) {
        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            //准备总账计算参数
            String exp = activity.getResouceConfig(key).getComputeExpressions();
            List<String> fileHeader = activity.getSourceContext(key).getFileHeader();
            List<String> totalKeys = activity.getResouceConfig(key).getCompareTotalKeys();

            BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
            while (line != null) {
                totalAmount = totalAmount.add(countTotal(new InToPost(exp), line, fileHeader, totalKeys));
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            activity.getSourceContext(key).setTotalAmount(totalAmount);
        } catch (IOException e) {
            log.error("ExternalSortUtil#countSingleFileTotal error,filePath:{}", filePath);
        }

    }

    /**
     * 计算每行的总账
     *
     * @param inToPost
     * @param line
     * @param fileHeader
     * @param totalKeys
     * @return
     */
    public BigDecimal countTotal(InToPost inToPost, String line, List<String> fileHeader, List<String> totalKeys) {
        Map<String, BigDecimal> dataMap = new HashMap<>();
        String[] strs = line.split(LOCAL_FILE_SPLIT);
        for (String key : totalKeys) {
            dataMap.put(key, new BigDecimal(strs[fileHeader.indexOf(key)]));
        }
        String explain = inToPost.doTransWithReplaceKey(dataMap);
        ParsePost parsePost = new ParsePost(explain);
        BigDecimal res = parsePost.doParse();

        return res;
    }

}
