package com.finance.anubis.adapter

import com.guming.api.json.JsonUtil
import com.finance.anubis.core.task.model.OffLineActivityResult
import com.finance.anubis.req.OffLineActivityResultReq
import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 19:25
 * @Description
 * */
class OffLineActivityResultAdapterSpec extends BaseSpecification {


    def adapt2OffLineActivityResult() {
        given:
        def req = new OffLineActivityResultReq()
        req.setId(1)
        req.setTaskActivityId(1)
        req.setBizKey("bizKey")
        req.setResultType("TOTAL")
        req.setVerifyResult("VARIANCE")
        when:
        def res = OffLineActivityResultAdapter.adapt2OffLineActivityResult(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2OffLineTaskTotalResultDTO() {
        given:
        def json = "{\"id\":140,\"taskActivityId\":120,\"createTime\":\"2023-03-18T14:39:40\",\"updateTime\":\"2023-03-18T14:39:40\",\"resultType\":\"TOTAL\",\"compareData\":{\"resultInfoType\":\"TOTAL\",\"sourceTotal\":1599,\"targetTotal\":36000},\"compareKeys\":{\"sourceCompareKeys\":[\"orderAmt\"],\"targetCompareKeys\":[\"orderAmt\"]},\"verifyResult\":\"VARIANCE\",\"bizKey\":\"DOU_YIN_ORDER_WECHAT_2023-03-18\",\"times\":1}"
        def result = JsonUtil.of(json, OffLineActivityResult.class)
        when:
        def res = OffLineActivityResultAdapter.adapt2OffLineTaskTotalResultDTO(result)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2OffLineTaskDetailResultDTO() {
        given:
        def json = "{\"id\":141,\"taskActivityId\":120,\"createTime\":\"2023-03-18T14:39:41\",\"updateTime\":\"2023-03-18T14:39:41\",\"resultType\":\"DETAIL\",\"compareData\":{\"resultInfoType\":\"DETAIL\",\"sourceUnique\":[],\"targetUnique\":[\"2023031815011501814\",\"202303186591053556\",\"2023031810798355569\",\"2023031817042579171\",\"202303184702196498\",\"202303184702196496\",\"2023031811485148462\"],\"inConsistency\":[]},\"compareKeys\":{\"sourceCompareKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"targetCompareKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"]},\"verifyResult\":\"VARIANCE\",\"bizKey\":\"DOU_YIN_ORDER_WECHAT_2023-03-18\",\"times\":1}"
        def result = JsonUtil.of(json, OffLineActivityResult.class)
        when:
        def res = OffLineActivityResultAdapter.adapt2OffLineTaskDetailResultDTO(result)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2OffLineActivityResultRes() {
        given:
        def totalJson = "{\"id\":140,\"taskActivityId\":120,\"createTime\":\"2023-03-18T14:39:40\",\"updateTime\":\"2023-03-18T14:39:40\",\"resultType\":\"TOTAL\",\"compareData\":{\"resultInfoType\":\"TOTAL\",\"sourceTotal\":1599,\"targetTotal\":36000},\"compareKeys\":{\"sourceCompareKeys\":[\"orderAmt\"],\"targetCompareKeys\":[\"orderAmt\"]},\"verifyResult\":\"VARIANCE\",\"bizKey\":\"DOU_YIN_ORDER_WECHAT_2023-03-18\",\"times\":1}"
        def totalResult = JsonUtil.of(totalJson, OffLineActivityResult.class)
        def detailJson = "{\"id\":141,\"taskActivityId\":120,\"createTime\":\"2023-03-18T14:39:41\",\"updateTime\":\"2023-03-18T14:39:41\",\"resultType\":\"DETAIL\",\"compareData\":{\"resultInfoType\":\"DETAIL\",\"sourceUnique\":[],\"targetUnique\":[\"2023031815011501814\",\"202303186591053556\",\"2023031810798355569\",\"2023031817042579171\",\"202303184702196498\",\"202303184702196496\",\"2023031811485148462\"],\"inConsistency\":[]},\"compareKeys\":{\"sourceCompareKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"targetCompareKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"]},\"verifyResult\":\"VARIANCE\",\"bizKey\":\"DOU_YIN_ORDER_WECHAT_2023-03-18\",\"times\":1}"
        def detailResult = JsonUtil.of(detailJson, OffLineActivityResult.class)
        when:
        def res = OffLineActivityResultAdapter.adapt2OffLineActivityResultRes(totalResult, detailResult)
        then:
        println(JsonUtil.toJson(res))
    }

}
