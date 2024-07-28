package com.finance.anubis.repository.impl

import cn.hutool.extra.spring.SpringUtil
import com.finance.anubis.repository.dto.OffLineTaskActivityResultDTO
import com.finance.anubis.repository.entity.OffLineTaskActivityResultEntity
import com.finance.anubis.repository.mapper.OffLineTaskActivityResultMapper
import com.guming.finance.report.common.BaseSpecification
import mockit.Mock
import mockit.MockUp
import org.springframework.context.annotation.Import

import javax.sql.DataSource
import java.time.LocalDateTime

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 09:36
 * @Description
 * */
@Import([SpringUtil])
class OffLineActivityResultRepositorySpec extends BaseSpecification {

    com.finance.anubis.repository.OffLineActivityResultRepository offLineActivityResultRepository

    OffLineTaskActivityResultMapper activityResultMapper

    DataSource dataSource

    def result

    def setup() {
        dataSource = com.finance.anubis.utils.MapperUtil.inMemoryDataSource()
        activityResultMapper = com.finance.anubis.utils.MapperUtil.getMapper(OffLineTaskActivityResultMapper.class, dataSource)
        offLineActivityResultRepository = new OffLineActivityResultRepositoryImpl(activityResultMapper)
        com.finance.anubis.utils.MapperUtil.createAppMainTable(dataSource)
        new MockUp<OffLineTaskActivityResultDTO>() {
            @Mock
            static com.finance.anubis.core.task.model.OffLineActivityResult toModel(OffLineTaskActivityResultEntity entity) {
                com.finance.anubis.core.task.model.OffLineActivityResult res = new com.finance.anubis.core.task.model.OffLineActivityResult();
                res.setBizKey(entity.getBizKey())
                return res;
            }
        }
        result = new com.finance.anubis.core.task.model.OffLineActivityResult()
        result.setBizKey("DOU_YIN_ORDER_ALIPAY_2023")
        result.setTimes(1)
        result.setResultType(com.finance.anubis.core.constants.enums.OffLineActivityResultType.TOTAL)
        result.setCompareData(new com.finance.anubis.core.task.model.OffLineActivityResult.TotalResultInfo())
        result.setCompareKeys(new com.finance.anubis.core.task.model.OffLineActivityResult.ResultKey())
        result.setTaskActivityId(1L)
        result.setVerifyResult(com.finance.anubis.core.constants.enums.ActionResult.UNREADY)
        result.setCreateTime(LocalDateTime.now())
    }


    def "save"() {
        given:

        when:
        def res = offLineActivityResultRepository.save(result)
        then:
        res
    }

    def "saveNull"() {
        given:

        when:
        def res = offLineActivityResultRepository.save(null)
        then:
        !res
    }

    def "saveFail"() {
        given:
        OffLineTaskActivityResultMapper mapper = Mock()
        mapper.insert(_) >> 0
        offLineActivityResultRepository = new OffLineActivityResultRepositoryImpl(mapper)
        when:
        def res = offLineActivityResultRepository.save(result)
        then:
        !res
    }

    def "update"() {
        given:
        offLineActivityResultRepository.save(result)
        when:
        result.setVerifyResult(com.finance.anubis.core.constants.enums.ActionResult.VARIANCE)
        def res = offLineActivityResultRepository.update(result)
        then:
        res
    }

    def "updateNull"() {
        given:

        when:
        result.setVerifyResult(com.finance.anubis.core.constants.enums.ActionResult.VARIANCE)
        def res = offLineActivityResultRepository.update(null)
        then:
        !res
    }

    def "updateFail"() {
        given:
        OffLineTaskActivityResultMapper mapper = Mock()
        mapper.updateByPrimaryKey(_) >> 0
        offLineActivityResultRepository = new OffLineActivityResultRepositoryImpl(mapper)
        when:
        def res = offLineActivityResultRepository.update(result)
        then:
        !res
    }

    def "selectTotalResult"() {
        given:
        offLineActivityResultRepository.save(result)
        when:
        //每次不会清空,暂时面向整个类跑单测
//        def res = offLineActivityResultRepository.selectTotalResult(result.getId())
        def res = offLineActivityResultRepository.selectTotalResult(1L)
        then:
        res.getBizKey().equals(result.getBizKey())
    }

    def "selectTotalResultNullId"() {
        given:
        offLineActivityResultRepository.save(result)
        when:
        def res = offLineActivityResultRepository.selectTotalResult(null)
        then:
        res == null
    }

    def "selectTotalResultFail"() {
        given:
        offLineActivityResultRepository.save(result)
        when:
        def res = offLineActivityResultRepository.selectTotalResult(2L)
        then:
        res == null
    }
    def "selectDetailResult"() {
        given:
        result.setResultType(com.finance.anubis.core.constants.enums.OffLineActivityResultType.DETAIL)
        offLineActivityResultRepository.save(result)
        when:
        //每次不会清空,暂时面向整个类跑单测
//        def res = offLineActivityResultRepository.selectTotalResult(result.getId())
        def res = offLineActivityResultRepository.selectDetailResult(1L)
        then:
        res.getBizKey().equals(result.getBizKey())
    }

    def "selectDetailResultNullId"() {
        given:
        result.setResultType(com.finance.anubis.core.constants.enums.OffLineActivityResultType.DETAIL)
        offLineActivityResultRepository.save(result)
        when:
        def res = offLineActivityResultRepository.selectDetailResult(null)
        then:
        res == null
    }

    def "selectDetailResultFail"() {
        given:
        result.setResultType(com.finance.anubis.core.constants.enums.OffLineActivityResultType.DETAIL)
        offLineActivityResultRepository.save(result)
        when:
        def res = offLineActivityResultRepository.selectDetailResult(2L)
        then:
        res == null
    }
}
