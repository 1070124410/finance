package com.finance.anubis.repository.impl

import cn.hutool.extra.spring.SpringUtil
import com.guming.api.json.JsonUtil
import com.guming.common.exception.StatusCodeException
import com.finance.anubis.core.config.OffLineResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.repository.entity.TaskActivityEntity
import com.finance.anubis.repository.entity.TaskConfigEntity
import com.finance.anubis.repository.mapper.OffLineTaskActivityMapper
import com.finance.anubis.repository.mapper.TaskConfigMapper
import com.guming.finance.report.common.BaseSpecification
import mockit.Mock
import mockit.MockUp
import org.springframework.context.annotation.Import

import javax.sql.DataSource
import java.time.LocalDateTime

/**
 * @Author yezhaoyang
 * @Date 2023/03/13 21:47
 * @Description
 * */
@Import([SpringUtil])
class OffLineTaskActivityRepositorySpec extends BaseSpecification {

    com.finance.anubis.repository.OffLineTaskActivityRepository offLineTaskActivityRepository


    OffLineTaskActivityMapper offLineTaskActivityMapper

    TaskConfigMapper taskConfigMapper;

    DataSource dataSource

    com.finance.anubis.core.task.model.OffLineTaskActivity taskActivity

    def setup() {
        dataSource = com.finance.anubis.utils.MapperUtil.inMemoryDataSource()
        offLineTaskActivityMapper = com.finance.anubis.utils.MapperUtil.getMapper(OffLineTaskActivityMapper.class, dataSource)
        taskConfigMapper = com.finance.anubis.utils.MapperUtil.getMapper(TaskConfigMapper.class, dataSource)
        offLineTaskActivityRepository = new OffLineTaskActivityRepositoryImpl(offLineTaskActivityMapper, taskConfigMapper)
        com.finance.anubis.utils.MapperUtil.createAppMainTable(dataSource)

        def sourceConfig = "{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100}"
        def targetConfig = "{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100}"
        def taskConfig = new OffLineTaskConfig()
        taskConfig.setSourceConfig(JsonUtil.of(sourceConfig, OffLineResourceConfig.class))
        taskConfig.setTargetConfig(JsonUtil.of(targetConfig, OffLineResourceConfig.class))
        taskConfig.setName("DOU_YIN_ORDER_ALIPAY")
        taskConfig.setId(1L)
        taskActivity = com.finance.anubis.core.task.model.OffLineTaskActivity.init()
        taskActivity.setTaskConfig(taskConfig)
        taskActivity.initSourceContext()
        String sourceContextJson = "{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\"},\"pathList\":[],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"totalAmount\":0,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.256\"}"
        taskActivity.getContext().getSourceContext().put("source", JsonUtil.of(sourceContextJson, com.finance.anubis.core.context.OffLineActivityContext.SourceContext.class))
        def targetContextJson = "{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"totalAmount\":3000,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.291\"}"
        taskActivity.getContext().getSourceContext().put("target", JsonUtil.of(targetContextJson, com.finance.anubis.core.context.OffLineActivityContext.SourceContext.class))
        taskActivity.setId(1L)
        taskActivity.setVerifyDate(LocalDateTime.parse("2023-03-18T14:28:33.063").toString())

        new MockUp<com.finance.anubis.repository.dto.TaskActivityDTO>() {
            @Mock
            static com.finance.anubis.core.task.model.OffLineTaskActivity toOffLineModel(TaskActivityEntity taskActivityEntity, TaskConfigEntity taskConfigEntity) {
                com.finance.anubis.core.task.model.OffLineTaskActivity taskActivity = com.finance.anubis.core.task.model.OffLineTaskActivity.init()
                taskActivity.setAction(taskActivityEntity.getOffLineAction())
                taskActivity.setId(taskActivityEntity.getId())
                taskActivity.setTimes(taskActivityEntity.getTimes())
                taskActivity.setCreateTime(taskActivityEntity.getCreateTime())
                taskActivity.setUpdateTime(taskActivityEntity.getUpdateTime())
                return taskActivity;
            }
        }
    }


    void "testSaveTaskActivity"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_INIT)
        when:
        def save = offLineTaskActivityRepository.save(taskActivity)
        then:
        save
    }

    void "testSaveTaskActivityError"() {
        given:
        def save = false
        when:
        try {
            save = offLineTaskActivityRepository.save(null)
        } catch (StatusCodeException e) {
            save = true
        }
        then:
        save
    }

    void "testSaveTaskActivityFail"() {
        given:
        def activityMapper = Mock(OffLineTaskActivityMapper)
        activityMapper.insert(_) >> 0
        offLineTaskActivityRepository = new OffLineTaskActivityRepositoryImpl(activityMapper, taskConfigMapper)
        when:
        def save = offLineTaskActivityRepository.save(taskActivity)
        then:
        !save
    }

    void "testUpdateTaskActivity"() {
        given:
        offLineTaskActivityRepository.save(taskActivity)
        taskActivity.setCreateTime(LocalDateTime.now())
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_DONE)
        when:
        def update = offLineTaskActivityRepository.update(taskActivity)
        then:
        update
    }

    void "testUpdateTaskActivityNull"() {
        given:

        when:
        def save = true
        try {
            offLineTaskActivityRepository.update(null)
        } catch (Exception e) {
            save = false
        }
        then:
        !save
    }

    void "testUpdateTaskActivityFail"() {
        given:
        offLineTaskActivityRepository.save(taskActivity)
        def activityMapper = Mock(OffLineTaskActivityMapper)
        activityMapper.updateByPrimaryKey(_) >> 0
        offLineTaskActivityRepository = new OffLineTaskActivityRepositoryImpl(activityMapper, taskConfigMapper)
        when:
        def update = offLineTaskActivityRepository.update(taskActivity)
        then:
        !update
    }


    void "testGetByBizKey"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.getByBizKey(taskActivity.getBizKey())
        then:
        res.getAction().equals(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
    }

    void "testGetByBizKeyNull"() {
        given:

        when:
        def res = offLineTaskActivityRepository.getByBizKey(null)
        then:
        res == null
    }

    void "testGetByBizKeyNullBizKey"() {
        given:

        when:
        def res = offLineTaskActivityRepository.getByBizKey("123")
        then:
        res == null
    }


    void "testToDataFetch"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:

        def res = offLineTaskActivityRepository.toDataFetch(taskActivity.getId(), null, taskActivity.getAction())
        then:
        res
    }

    void "testToDataFetchNullId"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataFetch(taskActivity.getId(), null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataFetchNullParam"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataFetch(null, null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataFetchFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        offLineTaskActivityRepository.save(taskActivity)
        def res = offLineTaskActivityRepository.toDataFetch(taskActivity.getId(), null, taskActivity.getAction())
        then:
        !res
    }

    void "testToFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toFail(taskActivity.getId(), taskActivity.getAction())
        then:
        res
    }

    void "testToFailNullId"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toFail(taskActivity.getId(), null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToFailNullParam"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toFail(null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToFailFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toFail(taskActivity.getId(), taskActivity.getAction())
        then:
        !res
    }

    void "testToDataCompareDetail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataCompareDetail(taskActivity.getId(), taskActivity.getAction())
        then:
        res
    }

    void "testToDataCompareDetailNullId"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataCompareDetail(null, taskActivity.getAction())
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataCompareDetailNullParam"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataCompareDetail(null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataCompareDetailFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataCompareDetail(taskActivity.getId(), taskActivity.getAction())
        then:
        !res
    }

    void "testToDataCompareTotal"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataCompareTotal(taskActivity.getId(), taskActivity.getAction())
        then:
        res
    }

    void "testToDataCompareTotalNullId"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataCompareTotal(null, taskActivity.getAction())
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataCompareTotalNullParam"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_FETCH)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataCompareTotal(null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataCompareTotalFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataCompareTotal(taskActivity.getId(), taskActivity.getAction())
        then:
        !res
    }

    void "testToDataDone"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_DETAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataDone(taskActivity.getId(), taskActivity.getAction())
        then:
        res
    }

    void "testToDataDoneNullId"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_DETAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataDone(null, taskActivity.getAction())
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataDoneNullParam"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_DETAIL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = true
        try {
            offLineTaskActivityRepository.toDataDone(null, null)
        } catch (Exception e) {
            res = false
        }
        then:
        !res
    }

    void "testToDataDoneFail"() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL)
        offLineTaskActivityRepository.save(taskActivity)
        when:
        def res = offLineTaskActivityRepository.toDataDone(taskActivity.getId(), taskActivity.getAction())
        then:
        !res
    }
}
