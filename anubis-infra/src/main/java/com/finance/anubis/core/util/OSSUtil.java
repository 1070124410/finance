package com.finance.anubis.core.util;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.finance.anubis.core.config.OSSProperties;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

/**
 * oss 上传
 *
 * @author: linjuanjuan
 * @time: 6/17/21 11:17 AM
 */
@Component
public class OSSUtil {
    public final static Logger log = LoggerFactory.getLogger(OSSUtil.class);
    private static final String SLANT = "/";
    private OSSClientBuilder builder = new OSSClientBuilder();
    private OSS oss;
    @Autowired
    private Environment env;
    @Autowired
    private OSSProperties ossProperties;

    @PostConstruct
    public void init() {
        String property = this.env.getProperty("spring.profiles.active");
        if (StringUtils.isNotBlank(property) && property.equals("loc")) {
            this.oss = builder.build(ossProperties.getExternalEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        } else {
            this.oss = builder.build(ossProperties.getInternalEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    public String uploadFile(InputStream inputStream, String filePath, String fileName) {
        String path = getPath(filePath, fileName);
        oss.putObject(ossProperties.getFileBucketName(), path, inputStream);
        return path;
    }

    /**
     * 下载文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public InputStream downloadFile(String filePath, String fileName) {
        String path = getPath(filePath, fileName);
        OSSObject object = oss.getObject(ossProperties.getFileBucketName(), path);
        return object.getObjectContent();
    }

    /**
     * 下载文件
     *
     * @param filePath
     * @return
     */
    public InputStream downloadFile(String filePath) {
        OSSObject object = oss.getObject(ossProperties.getFileBucketName(), filePath);
        return object.getObjectContent();
    }

    /**
     * 获取url
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        return getUrl(key, false);
    }

    /**
     * 获取url
     *
     * @param key
     * @param isInternal
     * @return
     */
    public String getUrl(String key, boolean isInternal) {
        Date expiration = new Date(System.currentTimeMillis() + 315360000000L);
        URL url = oss.generatePresignedUrl(ossProperties.getFileBucketName(), key, expiration);
        if (url != null) {
            if (isInternal) {
                return url.toString();
            }
            String resultUrl = url.toString().replaceAll(ossProperties.getInternalEndPoint(), ossProperties.getExternalEndPoint());
            return resultUrl;
        } else {
            return null;
        }
    }


    /**
     * 获取文件夹
     */
    public String getPath(String filePath, String fileName) {
        return ossProperties.getFolder() + SLANT + filePath + SLANT + fileName;
    }

    /**
     * 判断文件是否存在
     */
    public boolean isExist(String filePath, String fileName) {
        return oss.doesObjectExist(ossProperties.getFileBucketName(), getPath(filePath, fileName));
    }

    /**
     * 判断文件是否存在
     */
    public boolean isExist(String filePath) {
        return oss.doesObjectExist(ossProperties.getFileBucketName(), filePath);
    }


    public Optional<File> downloadFileToLocal(String fileUrl, String LocalFilePath) throws IOException {
        try {
            File file = new File(LocalFilePath);
            InputStream is = downloadFile(fileUrl);
            FileUtil.writeFromStream(is, file);
            is.close();
            return Optional.of(file);
        } catch (Exception e) {
            log.error("OSSUtil#downloadFileToLocal error,fileUrl:{},LocalFilePath:{}", fileUrl, LocalFilePath);
            throw e;
        }
    }

    /**
     * 删除文件
     * objectName key 地址
     *
     * @param filePath
     */
    public Boolean delFile(String filePath, String fileName) {
        boolean exist = isExist(filePath, fileName);
        if (!exist) {
            return true;
        }
        oss.deleteObject(filePath, fileName);
        oss.shutdown();
        return true;
    }
}
