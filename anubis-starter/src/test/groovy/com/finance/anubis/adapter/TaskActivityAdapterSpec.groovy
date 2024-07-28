package com.finance.anubis.adapter

import com.guming.api.json.JsonUtil
import com.finance.anubis.core.task.model.TaskActivity
import com.finance.anubis.req.TaskActivityReq
import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 09:51
 * @Description
 * */
class TaskActivityAdapterSpec extends BaseSpecification {


    def adapt2TaskActivity(){
        given:
        def req=new TaskActivityReq()
        req.setId(1)
        req.setAction("SOUCE_FETCH")
        when:
        def res=TaskActivityAdapter.adapt2TaskActivity(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2TaskActivityRes(){
        given:
        def json="{\"id\":7,\"createTime\":\"2023-02-01T18:19:58\",\"updateTime\":\"2023-02-02T09:35:22\",\"context\":{\"sourceContext\":{\"sourceKey\":\"116_COUPON_EVENT\",\"messageSourceContext\":{\"sourceKey\":\"116_COUPON_EVENT\",\"body\":\"{     \\\"couponUserId\\\": \\\"1040047128256927081\\\",     \\\"orderId\\\": \\\"20221016004\\\",     \\\"type\\\": \\\"1\\\",     \\\"number\\\": \\\"19281\\\" }\",\"tags\":[\"byte_dance_coupon_tag\"],\"mappedData\":{\"orderId\":\"20221016004\",\"type\":\"1\",\"couponUserId\":\"1040047128256927081\",\"number\":\"19281\"},\"topic\":\"live_coupon_write_off_116\",\"group\":\"GID_CONSIST_GROUP\",\"resourceType\":\"MessageResourceConfig\"},\"urlSourceContexts\":[{\"sourceKey\":\"GET_WRITEOff_STATE\",\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryLocal\",\"response\":\"{\\\"code\\\":0,\\\"data\\\":{\\\"couponUserId\\\":\\\"1040047128256927081\\\",\\\"writeOffQuantity\\\":1}}\",\"mappedData\":{\"coupon_user_id\":\"1040047128256927081\",\"writeOffQuantity\":1},\"resourceType\":\"URLResourceConfig\"}],\"mappedData\":{\"orderId\":\"20221016004\",\"coupon_user_id\":\"1040047128256927081\",\"type\":\"1\",\"couponUserId\":\"1040047128256927081\",\"number\":\"19281\",\"writeOffQuantity\":1},\"resourceType\":\"EventResourceConfig\"},\"taskId\":1,\"targetContexts\":[{\"sourceKey\":\"GET_WRITEOff_STATE\",\"url\":\"https://liq-service.dev2-debug.iguming.net/coupon/queryTarget\",\"response\":\"{\\\"code\\\":0,\\\"data\\\":{\\\"couponUserId\\\":\\\"1040047128256927081\\\",\\\"writeOffQuantity\\\":0,\\\"withholdQuantity\\\":1,\\\"startReminder\\\":false}}\",\"mappedData\":{\"coupon_user_id\":\"1040047128256927081\",\"writeOffQuantity\":0},\"resourceType\":\"URLResourceConfig\"}]},\"action\":\"DONE\",\"onLineTaskConfig\":{\"id\":1,\"unique_keys\":[\"coupon_user_id\"],\"name\":\"couponCompare\"},\"times\":2}"
        def activity=JsonUtil.of(json, TaskActivity.class)
        when:
        def res=TaskActivityAdapter.adapt2TaskActivityRes(activity)
        then:
        println(JsonUtil.toJson(res))
    }

}
