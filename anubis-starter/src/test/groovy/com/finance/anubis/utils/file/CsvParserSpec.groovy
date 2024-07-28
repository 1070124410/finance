package com.finance.anubis.utils.file

import cn.hutool.extra.spring.SpringUtil
import com.finance.anubis.core.util.file.CsvParser
import com.finance.anubis.report.common.BaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
/**
 * @Author yezhaoyang
 * @Date 2023/03/15 13:32
 * @Description
 * */
@Import([SpringUtil, CsvParser])
class CsvParserSpec extends BaseSpecification {

    @Autowired
    CsvParser csvParser

    def "parse"() {
        given:
        def data = "123abc"
        when:
        def res = csvParser.parseLine(data)
        then:
        res.equals(data)
    }

}