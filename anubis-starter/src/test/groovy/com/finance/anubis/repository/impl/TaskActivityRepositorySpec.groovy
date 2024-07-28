package com.finance.anubis.repository.impl


import cn.hutool.extra.spring.SpringUtil
import com.alibaba.fastjson.JSON
import com.guming.api.json.JsonUtil
import com.guming.api.pojo.page.Limit
import com.guming.common.exception.StatusCodeException
import com.finance.anubis.core.config.OnLineTaskConfig
import com.finance.anubis.repository.dto.TaskConfigDTO
import com.finance.anubis.repository.entity.TaskActivityEntity
import com.finance.anubis.repository.entity.TaskConfigEntity
import com.finance.anubis.repository.mapper.TaskActivityMapper
import com.finance.anubis.repository.mapper.TaskConfigMapper
import com.guming.finance.report.common.BaseSpecification
import mockit.Mock
import mockit.MockUp
import org.springframework.context.annotation.Import

import java.time.LocalDateTime

/**
 * @Author yezhaoyang
 * @Date 2023/01/13 11:42
 * @Description
 * */
@Import([SpringUtil])
class TaskActivityRepositorySpec extends BaseSpecification {

    def dataSource

    com.finance.anubis.repository.TaskActivityRepository taskActivityRepository

    def taskActivityMapper

    def taskConfigMapper

    def taskActivity

    def taskConfigEntity

    def setup() {
        dataSource = com.finance.anubis.utils.MapperUtil.inMemoryDataSource()
        taskActivityMapper = com.finance.anubis.utils.MapperUtil.getMapper(TaskActivityMapper.class, dataSource)
        taskConfigMapper = com.finance.anubis.utils.MapperUtil.getMapper(TaskConfigMapper.class, dataSource)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        com.finance.anubis.utils.MapperUtil.createAppMainTable(dataSource)
        def contextJson = "{\"sourceContext\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"mappedData\":{\"couponUserId\":1040054133995451786,\"number\":19406,\"orderId\":1010054330198018786,\"type\":1},\"messageSourceContext\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"resourceType\":\"MessageResourceConfig\",\"mappedData\":{\"couponUserId\":1040054133995451786,\"number\":19406,\"orderId\":1010054330198018786,\"type\":1},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"liq_coupon_verify_topic\",\"tags\":[\"byte_dance_coupon_tag\"],\"body\":\"{\\\"couponUserId\\\":1040054133995451786,\\\"number\\\":19406,\\\"orderId\\\":1010054330198018786,\\\"type\\\":1}\"},\"urlSourceContexts\":[{\"sourceKey\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"mappedData\":{},\"url\":\"http://liq-service/coupon/queryLocal\",\"response\":\"{\\\"code\\\":0,\\\"data\\\":{}}\"}]},\"targetContexts\":[{\"sourceKey\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"mappedData\":{\"couponUserId\":\"1040054133995451786\",\"writeOffQuantity\":1},\"url\":\"http://platform-promotion-center/platform/promotion/coupon/query\",\"response\":\"{\\\"code\\\":0,\\\"data\\\":{\\\"couponUserId\\\":\\\"1040054133995451786\\\",\\\"couponId\\\":\\\"1625775137391267841\\\",\\\"promotionId\\\":\\\"1625775137391267841\\\",\\\"templateId\\\":5,\\\"effectiveStartTime\\\":\\\"2023-02-15 00:00:00\\\",\\\"effectiveEndTime\\\":\\\"2023-02-28 23:59:59\\\",\\\"couponName\\\":\\\"结算券-全场折扣\\\",\\\"type\\\":0,\\\"quota\\\":9.0,\\\"orderCondition\\\":\\\"无门槛\\\",\\\"productCondition\\\":\\\"全场饮品可用\\\",\\\"couponSecondType\\\":3,\\\"level\\\":1,\\\"status\\\":1,\\\"deliveryTypes\\\":[\\\"SELF_PICK\\\",\\\"TAKE_OUT\\\"],\\\"promotionExtraJson\\\":\\\"{\\\\\\\"remark\\\\\\\":[{\\\\\\\"label\\\\\\\":\\\\\\\"适用商品\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"全部饮品，加料，免费加料可用\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"适用门店\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"全部门店可用\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"适用渠道\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"微信小程序，支付宝小程序，抖音小程序\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"下单方式\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"自提、外卖配送\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"优惠说明\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"凭此券下单可享受9.0折优惠，最高优惠100元\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"活动规则\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"此券不与其他优惠同享，单笔订单限用1张，优惠部分金额不参与活力值和积分累积\\\\\\\"},{\\\\\\\"label\\\\\\\":\\\\\\\"退款说明\\\\\\\",\\\\\\\"value\\\\\\\":\\\\\\\"如使用本券下单后发生退单，退款以实付金额为准，本券返还至小程序卡券包\\\\\\\"}]}\\\",\\\"isApplyEffectiveTime\\\":true,\\\"writeOffQuantity\\\":1,\\\"withholdQuantity\\\":0,\\\"startReminder\\\":false,\\\"endReminder\\\":false,\\\"singleProductVoucherAndReductionTo\\\":false,\\\"receiveTime\\\":\\\"2023-02-15 16:34:22\\\"}}\"}],\"taskId\":1}"
        def context = JsonUtil.of(contextJson, com.finance.anubis.core.context.ActivityContext.class)
        def configJson = "{\"sourceConfig\":{\"key\":\"LIQ_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"orderId\":\"\$.orderId\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"liq_coupon_verify_topic\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://liq-service/coupon\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://platform-promotion-center/query\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}],\"compareKeys\":[\"couponUserId\"],\"delay\":1,\"uniqueKeys\":[\"couponUserId\"],\"name\":\"liqCouponCompare\",\"id\":1,\"version\":0}"
        taskActivity = new com.finance.anubis.core.task.model.TaskActivity()
        taskActivity.setContext(context)
        taskActivity.setOnLineTaskConfig(JSON.parseObject(configJson, OnLineTaskConfig.class))
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.SOURCE_FETCH)
        taskActivity.setCreateTime(LocalDateTime.now())
        taskActivity.setTimes(1)
        taskConfigEntity = TaskConfigDTO.toEntity(taskActivity.getOnLineTaskConfig())
        taskConfigEntity.setCreateTime(LocalDateTime.now())
        taskConfigEntity.setUpdateTime(LocalDateTime.now())
        taskConfigEntity.setId(1)
        taskConfigMapper.insert(taskConfigEntity)

        new MockUp<com.finance.anubis.repository.dto.TaskActivityDTO>() {
            @Mock
            static com.finance.anubis.core.task.model.TaskActivity toModel(TaskActivityEntity taskActivityEntity, TaskConfigEntity taskConfigEntity) {
                def res = new com.finance.anubis.core.task.model.TaskActivity()
                res.setAction(taskActivityEntity.getAction())
                return res;
            }
        }
    }

    def testSaveTaskActivity() {
        given:

        when:
        def res = taskActivityRepository.save(taskActivity)
        then:
        res
    }

    def testSaveTaskActivityNull() {
        given:
        when:
        try {
            taskActivityRepository.save(null)
        } catch (StatusCodeException e) {
            print(e.getStatusCode().getReason())
        }
        then:
        true
    }

    def testSaveTaskActivityFail() {
        given:
        def taskActivityMapper = Mock(TaskActivityMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskActivityMapper.insert(_) >> 0
        when:
        def res = taskActivityRepository.save(taskActivity)
        then:
        !res
    }

    def testUpdateTaskActivity() {
        given:
        taskActivityRepository.save(taskActivity)
        taskActivity.setTimes(21)
        when:
        def res = taskActivityRepository.update(taskActivity)
        then:
        res
    }

    def testUpdateTaskActivityNull() {
        given:
        when:
        try {
            taskActivityRepository.update(null)
        } catch (StatusCodeException e) {
            print(e.getStatusCode().getReason())
        }
        then:
        true
    }

    def testUpdateTaskActivityFail() {
        given:
        def taskActivityMapper = Mock(TaskActivityMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskActivityMapper.updateByPrimaryKey(_) >> 0
        when:
        def res = taskActivityRepository.update(taskActivity)
        then:
        !res
    }

    def testGetById() {
        given:
        //跑整个单测因未清理需要置为3 ,跑单个方法置为1
        def id = 3
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        taskActivityRepository.save(taskActivity)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskConfigMapper.selectByPrimaryKey(_) >> taskConfigEntity
        when:
        def res = taskActivityRepository.getById(id)
        then:
        res.getAction().equals(com.finance.anubis.core.constants.enums.Action.DONE)
    }

    def testGetByIdNull() {
        given:
        //跑整个单测因未清理需要置为4 ,跑单个方法置为1
        def id = 1
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        taskActivityRepository.save(taskActivity)
        when:
        def res = taskActivityRepository.getById(100)
        then:
        res == null
    }

    def testGetByBizKey() {
        given:
        //跑整个单测因未清理需要置为4 ,跑单个方法置为1
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        taskActivityRepository.save(taskActivity)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskConfigMapper.selectByPrimaryKey(_) >> taskConfigEntity
        when:
        def res = taskActivityRepository.getByBizKey(taskActivity.getBizKey())
        then:
        res.getAction().equals(com.finance.anubis.core.constants.enums.Action.DONE)
    }

    def testGetByBizKeyNull() {
        given:
        when:
        def res = taskActivityRepository.getByBizKey("")
        then:
        res == null
    }

    def testGetByBizKeyNullActivity() {
        given:
        when:
        def res = taskActivityRepository.getByBizKey("123")
        then:
        res == null
    }

    def getByParams() {
        given:
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        taskActivityRepository.save(taskActivity)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        def entity = new TaskActivityEntity()
        entity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        when:
        def res = taskActivityRepository.getByParams(entity)
        then:
        res.get(0).getAction().equals(com.finance.anubis.core.constants.enums.Action.DONE)
    }

    def getByParamsNull() {
        given:
        when:
        def res = taskActivityRepository.getByParams(null)
        then:
        res.size() == 0
    }

    def getByParamsEmpty() {
        given:
        def entity = new TaskActivityEntity()
        entity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        when:
        def res = taskActivityRepository.getByParams(entity)
        then:
        res.size() == 0
    }

    def testGetPageByParams() {
        given:
        Limit page = new Limit(1, 0, 2)
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        taskActivityRepository.save(taskActivity)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskActivityRepository = new TaskActivityRepositoryImpl(taskActivityMapper, taskConfigMapper)
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        def entity = new TaskActivityEntity()
        entity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        when:
        def res = taskActivityRepository.getPageByParams(page, entity)
        then:
        res.get(0).getAction().equals(com.finance.anubis.core.constants.enums.Action.DONE)
    }

    def testGetPageByParamsNull() {
        given:
        Limit page = new Limit(1, 0, 2)
        when:
        def res = taskActivityRepository.getPageByParams(page, null)
        then:
        res.size() == 0
    }

    def testGetPageByParamsEmpty() {
        given:
        Limit page = new Limit(1, 0, 2)
        def entity = new TaskActivityEntity()
        entity.setAction(com.finance.anubis.core.constants.enums.Action.COMPARE)
        when:
        def res = taskActivityRepository.getPageByParams(page, entity)
        then:
        res.size() == 0
    }
}
