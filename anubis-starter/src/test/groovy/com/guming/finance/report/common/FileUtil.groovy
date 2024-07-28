package com.guming.finance.report.common

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

/**
 * @author baocheng
 * @date 2023/1/12 20:38
 */
class FileUtil {

    static File getClasspathFile(String path) {
        return new File(FileUtil.getClassLoader().getResource(path).getFile())
    }

    static String getClasspathFileContent(String path) {
        return FileUtils.readFileToString(getClasspathFile(path), "UTF-8")
    }

    static boolean fileCompare(String actualPath, String expectClasspath) {
        def actualContent = DigestUtils.md5(new File(actualPath).getBytes())
        def exceptContent = DigestUtils.md5(new File(expectClasspath).getBytes())
        return actualContent == exceptContent
    }
}
