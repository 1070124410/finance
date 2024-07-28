package com.finance.anubis.adapter

import com.guming.api.json.JsonUtil
import com.finance.anubis.core.task.model.TaskActivityResult
import com.finance.anubis.req.ActivityResultReq
import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 09:51
 * @Description
 * */
class ActivityResultAdapterSpec extends BaseSpecification {


    def setup() {

    }

    def adapt2TaskActivityResult() {
        given:
        def req = new ActivityResultReq()
        req.setId(1)
        req.setTaskActivityId(1)
        req.setBizKey("bizKey")
        req.setActionResult("VARIANCE")
        when:
        def res = ActivityResultAdapter.adapt2TaskActivityResult(req)
        then:
        println(res)
    }

    def adapt2ActionResultRes() {
        given:
        def json = "{\"taskActivityId\":7,\"bizKey\":\"couponCompare_1040047128256927081\",\"times\":2,\"compareSourceData\":{\"writeOffQuantity\":1,\"coupon_user_id\":1040047128256927081},\"compareTargetData\":{\"writeOffQuantity\":0,\"coupon_user_id\":1040047128256927081},\"compareKeys\":[\"writeOffQuantity\",\"coupon_user_id\"],\"varianceKeys\":[\"writeOffQuantity\"],\"actionResult\":\"VARIANCE\",\"id\":3,\"createTime\":\"2023-03-24T09:59:39\",\"updateTime\":\"2023-03-24T09:59:39\"}"
        def result = JsonUtil.of(json, TaskActivityResult.class)
        when:
        def res = ActivityResultAdapter.adapt2ActionResultRes(result)
        then:
        println(res)
    }

}
