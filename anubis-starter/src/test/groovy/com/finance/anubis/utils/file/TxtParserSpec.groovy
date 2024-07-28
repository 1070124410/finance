package com.finance.anubis.utils.file

import cn.hutool.extra.spring.SpringUtil
import com.finance.anubis.core.constants.enums.FileType
import com.finance.anubis.core.util.file.TxtParser
import com.finance.anubis.report.common.BaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 13:14
 * @Description
 * */
@Import([SpringUtil, TxtParser])
class TxtParserSpec extends BaseSpecification {

    @Autowired
    TxtParser txtParser

    def "parse"() {
        given:
        def data = "123abc"
        when:
        def res = txtParser.parseLine(data)
        then:
        res.equals(data)
    }

    def "getFileType"(){
        given:
        when:
        txtParser.parseLine("data")
        then:
        txtParser.getFileType().equals(FileType.TXT)
    }
}
