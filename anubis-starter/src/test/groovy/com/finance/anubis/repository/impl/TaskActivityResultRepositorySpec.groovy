package com.finance.anubis.repository.impl

import cn.hutool.extra.spring.SpringUtil
import com.google.common.collect.Maps
import com.guming.api.pojo.page.Limit
import com.finance.anubis.repository.mapper.TaskActivityResultMapper
import com.finance.anubis.report.common.BaseSpecification
import org.springframework.context.annotation.Import

import java.time.LocalDateTime
/**
 * @Author yezhaoyang
 * @Date 2023/01/13 18:05
 * @Description
 * */
@Import([SpringUtil])
class TaskActivityResultRepositorySpec extends BaseSpecification {

    def dataSource

    def resultRepository

    def resultMapper

    def result


    def setup() {
        dataSource = com.finance.anubis.utils.MapperUtil.inMemoryDataSource()
        resultMapper = com.finance.anubis.utils.MapperUtil.getMapper(TaskActivityResultMapper.class, dataSource)
        resultRepository = new ActivityResultRepositoryImpl(resultMapper)
        com.finance.anubis.utils.MapperUtil.createAppMainTable(dataSource)
        result = new com.finance.anubis.core.task.model.TaskActivityResult()
        result.setId(1)
        result.setCreateTime(LocalDateTime.now())
        result.setUpdateTime(LocalDateTime.now())
        result.setTimes(1)
        result.setBizKey("liq_100")
        result.setTaskActivityId(1)
        result.setCompareKeys(Collections.singletonList("orderId"))
        def map = Maps.newHashMap()
        map.put("orderId", 1)
        result.setCompareSourceData(map)
        map.put("orderId", 2)
        result.setCompareTargetData(map)
        result.setVarianceKeys(Collections.singletonList("orderId"))
        result.setActionResult(com.finance.anubis.core.constants.enums.ActionResult.VARIANCE)
    }

    def save() {
        given:

        when:
        def res = resultRepository.save(result)
        then:
        res
    }

    def saveNull() {
        given:

        when:
        def res = resultRepository.save(null)
        then:
        !res
    }

    def saveFail() {
        given:
        def mapper = Mock(TaskActivityResultMapper)
        resultRepository = new ActivityResultRepositoryImpl(mapper)
        mapper.insert(_) >> 0
        when:
        def res = resultRepository.save(result)
        then:
        !res
    }

    def update() {
        given:
        resultRepository.save(result)
        result.setActionResult(com.finance.anubis.core.constants.enums.ActionResult.FAIL)
        when:
        def res = resultRepository.update(result)
        then:
        res
    }

    def updateNull() {
        given:
        when:
        def res = resultRepository.update(null)
        then:
        !res
    }

    def updateFail() {
        given:
        def mapper = Mock(TaskActivityResultMapper)
        resultRepository = new ActivityResultRepositoryImpl(mapper)
        mapper.updateByPrimaryKey(_) >> 0
        when:
        def res = resultRepository.update(result)
        then:
        !res
    }

    def selectByParams() {
        given:
        resultRepository.save(result)
        def result = new com.finance.anubis.core.task.model.TaskActivityResult()
        result.setActionResult(com.finance.anubis.core.constants.enums.ActionResult.VARIANCE)
        when:
        def res = resultRepository.selectByParams(result)
        then:
        res.size() == 1
    }

    def selectByParamsNull() {
        given:
        when:
        def res = resultRepository.selectByParams(null)
        then:
        res.size() == 0
    }

    def selectByParamsEmpty() {
        given:
        when:
        def res = resultRepository.selectByParams(result)
        then:
        res.size() == 0
    }

    def selectPageByParams() {
        given:
        resultRepository.save(result)
        def limit = new Limit(1, 0, 2)
        def result = new com.finance.anubis.core.task.model.TaskActivityResult()
        result.setActionResult(com.finance.anubis.core.constants.enums.ActionResult.VARIANCE)
        when:
        def res = resultRepository.selectPageByParams(limit, result)
        then:
        res.size() == 1
    }

    def selectPageByParamsNull() {
        given:
        when:
        def res = resultRepository.selectPageByParams(null, null)
        then:
        res.size() == 0
    }

    def selectPageByParamsEmpty() {
        given:
        def limit = new Limit(1, 0, 2)
        when:
        def res = resultRepository.selectPageByParams(limit, result)
        then:
        res.size() == 0
    }

    def selectByUniqueInfo() {
        given:
        resultRepository.save(result)
        def result = new com.finance.anubis.core.task.model.TaskActivityResult()
        result.setBizKey("liq_100")
        result.setTaskActivityId(1)
        when:
        def res = resultRepository.selectByUniqueInfo(result)
        then:
        print(res)
    }

    def selectByUniqueInfoNullParam() {
        given:
        def result = new com.finance.anubis.core.task.model.TaskActivityResult()
        when:
        def res = resultRepository.selectByUniqueInfo(result)
        then:
        res == null
    }

    def selectByUniqueInfoNullRes() {
        given:
        resultRepository.save(result)
        def result = new com.finance.anubis.core.task.model.TaskActivityResult()
        result.setBizKey("liq_100")
        result.setTaskActivityId(100)
        when:
        def res = resultRepository.selectByUniqueInfo(result)
        then:
        res == null
    }
}
