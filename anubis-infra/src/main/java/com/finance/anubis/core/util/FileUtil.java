package com.finance.anubis.core.util;

import com.finance.anubis.utils.JsonUtil;
import lombok.CustomLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author yezhaoyang
 * @Date 2023/02/27 14:27
 * @Description
 **/
@Component
public class FileUtil {
    public final static Logger log = LoggerFactory.getLogger(FileUtil.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 流式下载文件
     *
     * @param filePath
     * @param url
     * @param method
     * @param requestParams
     * @return
     */
    public void streamDownload(String filePath, String url, HttpMethod method, Map<String, Object> requestParams) {
        if (!createFile(filePath)) {
            return;
        }
        StringBuilder httpUrl = buildHttpUrl(url, requestParams);
        RequestCallback requestCallback = request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getBody().write(JsonUtil.toJson(requestParams).getBytes(StandardCharsets.UTF_8));
        };

        // 对响应进行流式处理而不是将其全部加载到内存中
        Boolean flag = restTemplate.execute(httpUrl.toString(), method, requestCallback, clientHttpResponse -> {
            HttpStatus statusCode = clientHttpResponse.getStatusCode();
            if (!statusCode.is2xxSuccessful()) {
                log.error("FileUtil dowload fail,url:{}", url);
                return false;
            }
            InputStream inputStream = clientHttpResponse.getBody();
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.close();
            return true;
        }, requestParams);
        if (flag) {
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("FileUtil#streamDownload fail,filePath:{},url:{}", filePath, httpUrl);
            }
        }
    }

    /**
     * 将数据写入本地文件
     *
     * @param dataList
     */
    public void writeToLocal(List<String> dataList, String path) {
        cn.hutool.core.io.FileUtil.writeUtf8Lines(dataList, path);
    }

    /**
     * 删除本地文件
     *
     * @param pathList
     */
    public void deleteLocalFiles(List<String> pathList) {
        try {
            for (String path : pathList) {
                cn.hutool.core.io.FileUtil.del(path);
            }
        } catch (Exception e) {
            log.error("FileUtil#deleteLocalFiles fail,pathList:{}", pathList);
        }
    }

    public void renameLocalFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (newFile.exists()) {
            newFile.delete();
            log.info("FileUtil#rename error,newFile exist,oldPath:{},newPath:{}", oldPath, newPath);
        }
        oldFile.renameTo(newFile);
    }


    /**
     * 构建请求路径,可适用于RestTemplate的get或post
     *
     * @param url
     * @param requestParams
     * @return
     */
    public StringBuilder buildHttpUrl(String url, Map<String, Object> requestParams) {
        if (requestParams.isEmpty()) {
            return new StringBuilder(url);
        }
        StringBuilder urlParamFormatted = new StringBuilder(url + "?");
        String[] keyArray = requestParams.keySet().toArray(new String[0]);
        for (int i = 0; i < keyArray.length; i++) {
            String tmpKey = keyArray[i];
            urlParamFormatted.append(tmpKey).append("={").append(tmpKey).append("}");
            if (keyArray.length >= 2 && i < keyArray.length - 1) {
                urlParamFormatted.append("&");
            }
        }
        return urlParamFormatted;
    }

    public boolean ifExist(String path) {
        return cn.hutool.core.io.FileUtil.exist(path);
    }

    public boolean createFile(String path) {
        if (ifExist(path)) {
            return true;
        }
        try {
            File file = new File(path);
            boolean newFile = file.createNewFile();
            return newFile;
        } catch (IOException e) {
            log.error("createFile error,path:{}", path);
        }
        return false;
    }
}
