package com.finance.anubis.adapter

import com.guming.api.json.JsonUtil
import com.finance.anubis.core.task.model.Task
import com.finance.anubis.req.TaskReq
import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 09:52
 * @Description
 * */
class TaskAdapterSpec extends BaseSpecification {

    def adapt2TaskOnLine() {
        given:
        def json = "{\"id\":1,\"status\":\"STOP\",\"type\":\"ONLINE\",\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\",\"config\":{\"id\":1,\"sourceConfig\":{\"key\":\"116_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\",\"orderId\":\"\$.orderId\",\"type\":\"\$.type\",\"number\":\"\$.number\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"live_order_core_change_116\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryLocal\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"},\"orderId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"orderId\"},\"type\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"type\"},\"number\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"number\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryTarget\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"}}}],\"compareKeys\":[\"writeOffQuantity\",\"coupon_user_id\"],\"delay\":0,\"uniqueKeys\":[\"coupon_user_id\"],\"name\":\"couponCompare\",\"version\":0}}"
        def req = JsonUtil.of(json, TaskReq.class)
        when:
        def res = TaskAdapter.adapt2Task(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2TaskOffLine() {
        given:
        def json = "{\"id\":1,\"status\":\"STOP\",\"type\":\"OFFLINE\",\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\",\"offLineConfig\":{\"id\":5,\"sourceConfig\":{\"key\":\"source\",\"url\":\"http: //liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000},\"targetConfig\":{\"key\":\"target\",\"url\":\"http: //liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000},\"uniqueKeys\":[\"orderId\"],\"name\":\"DOU_YIN_ORDER_ALIPAY\",\"version\":0,\"errorThreshold\":500,\"detailSwitch\":1,\"retryTime\":3}}"
        def req = JsonUtil.of(json, TaskReq.class)
        when:
        def res = TaskAdapter.adapt2Task(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2Task() {
        given:
        def json = "{\"id\":1,\"status\":\"STOP\",\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\"}"
        def req = JsonUtil.of(json, TaskReq.class)
        when:
        def res = TaskAdapter.adapt2Task(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2OnLineTaskRes() {
        given:
        def json = "{\"id\":1,\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\",\"config\":{\"sourceConfig\":{\"key\":\"116_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\",\"orderId\":\"\$.orderId\",\"type\":\"\$.type\",\"number\":\"\$.number\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"live_order_core_change_116\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryLocal\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"},\"orderId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"orderId\"},\"type\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"type\"},\"number\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"number\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryTarget\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"}}}],\"compareKeys\":[\"writeOffQuantity\",\"coupon_user_id\"],\"delay\":0,\"uniqueKeys\":[\"coupon_user_id\"],\"name\":\"couponCompare\",\"id\":1,\"version\":0},\"status\":\"STOP\",\"taskType\":\"ONLINE\"}"
        def task = JsonUtil.of(json, Task.class)
        when:
        def res = TaskAdapter.adapt2TaskRes(task)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2OffLineTaskRes() {
        given:
        def json = "{\"id\":1,\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\",\"status\":\"STOP\",\"offLineTaskConfig\":{\"sourceConfig\":{\"key\":\"source\",\"url\":\"http: //liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"fetchDelay\":100000,\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":0,\"skipTail\":0},\"targetConfig\":{\"key\":\"target\",\"url\":\"http: //liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"fetchDelay\":100000,\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":0,\"skipTail\":0},\"errorThreshold\":500,\"detailSwitch\":true,\"uniqueKeys\":[\"orderId\"],\"retryTime\":3,\"name\":\"DOU_YIN_ORDER_ALIPAY\",\"id\":5,\"version\":0},\"taskType\":\"OFFLINE\"}"
        def task = JsonUtil.of(json, Task.class)
        when:
        def res = TaskAdapter.adapt2TaskRes(task)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2TaskEntity(){
        given:
        def json = "{\"id\":1,\"status\":\"STOP\",\"type\":\"ONLINE\",\"createTime\":\"2023-03-24T14:28:47\",\"updateTime\":\"2023-03-24T14:28:47\",\"config\":{\"id\":1,\"sourceConfig\":{\"key\":\"116_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\",\"orderId\":\"\$.orderId\",\"type\":\"\$.type\",\"number\":\"\$.number\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"live_order_core_change_116\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryLocal\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"},\"orderId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"orderId\"},\"type\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"type\"},\"number\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"number\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"writeOffQuantity\":\"\$.writeOffQuantity\",\"coupon_user_id\":\"\$.couponUserId\"},\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryTarget\",\"method\":\"GET\",\"requestParamMapping\":{\"couponUserId\":{\"sourceKey\":\"116_COUPON_EVENT\",\"path\":\"couponUserId\"}}}],\"compareKeys\":[\"writeOffQuantity\",\"coupon_user_id\"],\"delay\":0,\"uniqueKeys\":[\"coupon_user_id\"],\"name\":\"couponCompare\",\"version\":0}}"
        def req = JsonUtil.of(json, TaskReq.class)
        when:
        def res = TaskAdapter.adapt2TaskEntity(req)
        then:
        println(JsonUtil.toJson(res))
    }

}
