package com.finance.anubis.adapter

import com.guming.api.json.JsonUtil
import com.finance.anubis.core.task.model.OffLineTaskActivity
import com.finance.anubis.req.TaskActivityReq
import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/24 14:15
 * @Description
 * */
class OffLineTaskActivityAdapterSpec extends BaseSpecification {

    def adapt2TaskActivity() {
        given:
        def req = new TaskActivityReq()
        req.setId(1)
        req.setAction("DATA_FETCH")
        when:
        def res = OffLineTaskActivityAdapter.adapt2TaskActivity(req)
        then:
        println(JsonUtil.toJson(res))
    }

    def adapt2TaskActivityRes() {
        given:
        def json = "{\"id\":120,\"bizKey\":\"DOU_YIN_ORDER_WECHAT_2023-03-18\",\"createTime\":\"2023-03-18T14:39:40\",\"updateTime\":\"2023-03-18T14:39:41\",\"times\":1,\"action\":\"DATA_DONE\",\"context\":{\"sourceContext\":{\"source\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_wechat?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=LxSk4HryxtxcLY5nezbVaXyMCrc%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_wechat?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=LxSk4HryxtxcLY5nezbVaXyMCrc%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_WECHAT_2023-03-18/source_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_WECHAT_2023-03-18/source_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_WECHAT_2023-03-18/source_reconciliation\",\"totalAmount\":1599,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":5}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.174\"},\"target\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_wechat?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=PZ%2B3Hwj8g3lE6GYMmeuCdV%2FyDr8%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_wechat?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=PZ%2B3Hwj8g3lE6GYMmeuCdV%2FyDr8%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_WECHAT_2023-03-18/target_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_WECHAT_2023-03-18/target_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_WECHAT_2023-03-18/target_reconciliation\",\"totalAmount\":36000,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":5}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.213\"}},\"verifyDate\":\"2023-03-18T14:39:40.213\"},\"onLineTaskConfig\":{\"id\":9,\"createTime\":\"2023-03-09T18:18:07\",\"updateTime\":\"2023-03-09T18:18:08\",\"sourceConfig\":{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000},\"targetConfig\":{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000},\"errorThreshold\":500,\"detailSwitch\":1,\"compareKeys\":[\"amount\"],\"delay\":0,\"retryTime\":3,\"uniqueKeys\":[\"orderId\"],\"name\":\"DOU_YIN_ORDER_WECHAT\",\"version\":0}}"
        def activity = JsonUtil.of(json, OffLineTaskActivity.class)
        when:
        def res = OffLineTaskActivityAdapter.adapt2TaskActivityRes(activity)
        then:
        println(JsonUtil.toJson(res))
    }
}
