package com.finance.anubis.utils

import cn.hutool.extra.spring.SpringUtil
import com.finance.anubis.core.util.FileUtil
import com.guming.finance.report.common.BaseSpecification
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

import javax.annotation.Resource

/**
 * @Author yezhaoyang
 * @Date 2023/03/10 11:52
 * @Description
 * */

@Import([SpringUtil, FileUtil, RestTemplate])
class FileUtilSpec extends BaseSpecification {

    @Resource
    FileUtil fileUtil

    def path

    def setup() {
        path = this.getClass().getResource("/").getPath()
        //路径名 : 系统目录 + /anubis/anubis-starter/target/test-classes/
    }

    def "testDowload"() {
        given:
        String url = "https://p0.qhmsg.com/t015611ff4c80abe80f.jpg"
        String path = path + "dowload.png"
        when:
        fileUtil.streamDownload(path, url, HttpMethod.GET, new HashMap<String, Object>())
        then:
        true
    }


    def "testWriteToLocal"() {
        given:
        def filePath = path + "writeToLocal.txt"
        def file = new File(filePath)
        when:
        fileUtil.writeToLocal(Collections.singletonList("HelloWorld"), filePath)
        then:
        file.length() > 0
    }

    def "testRenameFile"() {
        given:
        def oldFilePath = path + "oldFile.txt"
        def newFilePath = path + "newFile.txt"
        fileUtil.writeToLocal(Collections.singletonList("oldFile"), oldFilePath)
        when:
        fileUtil.renameLocalFile(oldFilePath, newFilePath)
        def file = new File(newFilePath)
        then:
        file.exists()
    }

    def "testDeleteToLocal"() {
        given:
        def filePath = path + "waitDeleteFile.txt"
        fileUtil.writeToLocal(Collections.singletonList("wait delete"), filePath)
        def file = new File(filePath)
        when:
        fileUtil.deleteLocalFiles(Collections.singletonList(filePath))
        then:
        !file.exists()
    }

}
