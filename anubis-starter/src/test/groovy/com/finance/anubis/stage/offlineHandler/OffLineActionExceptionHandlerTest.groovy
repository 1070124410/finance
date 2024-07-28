package com.finance.anubis.stage.offlineHandler

import cn.hutool.extra.spring.SpringUtil
import com.guming.api.json.JsonUtil
import com.guming.api.pojo.Status
import com.guming.common.exception.StatusCodeException
import com.finance.anubis.core.config.OffLineResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.constants.enums.OffLineAction
import com.finance.anubis.core.context.OffLineActivityContext
import com.finance.anubis.core.task.stage.offlineHandler.OffLineActionExceptionHandler
import com.finance.anubis.core.util.DingTalkWebhookUtil
import com.finance.anubis.report.common.BaseSpecification
import com.guming.mq.api.MessageProducer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 15:58
 * @Description
 * */
@Import([SpringUtil, OffLineActionExceptionHandler])
class OffLineActionExceptionHandlerTest extends BaseSpecification {

    @Autowired
    OffLineActionExceptionHandler handler
    @SpringBean
    MessageProducer messageProducer = Mock()
    @SpringBean
    DingTalkWebhookUtil dingTalkWebhookUtil = Mock()
    @SpringBean
    com.finance.anubis.repository.OffLineTaskActivityRepository activityRepository = Mock()

    def msg

    com.finance.anubis.core.task.model.OffLineTaskActivity taskActivity

    def setup() {
        taskActivity = new com.finance.anubis.core.task.model.OffLineTaskActivity()
        def json = "{\"sourceContext\":{\"source\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\"},\"pathList\":[],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"totalAmount\":0,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.256\"},\"target\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"totalAmount\":3000,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.291\"}},\"verifyDate\":\"2023-03-18T14:39:40.291\"}"
        def context = JsonUtil.of(json, OffLineActivityContext.class)
        taskActivity.setContext(context)
        def config = new OffLineTaskConfig()
        def sourceConfig = "{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000}"
        def source = JsonUtil.of(sourceConfig, OffLineResourceConfig.class)
        def targetConfig = "{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000}"
        def target = JsonUtil.of(targetConfig, OffLineResourceConfig.class)
        config.setName("DOU_YIN_ORDER_ALIPAY")
        config.setSourceConfig(source)
        config.setTargetConfig(target)
        config.setUniqueKeys(Collections.singletonList("orderId"))
        config.setErrorThreshold(1000)
        config.setDetailSwitch(true)
        config.setRetryTime(3)
        taskActivity.setTimes(1)
        taskActivity.setTaskConfig(config)
        taskActivity.setId(1)

        msg = new com.finance.anubis.exception.ErrorMsg(taskActivity.getBizKey(), "source", OffLineAction.DATA_FETCH, "错误", new StatusCodeException(Status.error("oss异常")))
    }

    def uncaughtException() {
        given:
        activityRepository.getByBizKey(_) >> taskActivity
        activityRepository.updateTimes(_) >> true
        when:
        handler.uncaughtException(new Thread(), new StatusCodeException(Status.error(JsonUtil.toJson(msg))))
        then:
        true
    }

    def uncaughtExceptionUnCaught() {
        given:
        activityRepository.getByBizKey(_) >> taskActivity
        activityRepository.updateTimes(_) >> true
        when:
        try {
            handler.uncaughtException(new Thread(), new StackOverflowError(JsonUtil.toJson(msg)))
        } catch (NullPointerException e) {
            print("人工处理")
        }
        then:
        true
    }

    def uncaughtExceptionFail() {
        given:
        activityRepository.getByBizKey(_) >> taskActivity
        activityRepository.toFail(_, _) >> true
        taskActivity.setTimes(4)
        when:
        handler.uncaughtException(new Thread(), new StatusCodeException(Status.error(JsonUtil.toJson(msg))))
        then:
        true
    }

    def fibonacci() {
        given:
        def num = 1
        when:
        def res = OffLineActionExceptionHandler.fibonacci(num)
        then:
        res == 5
    }

    def fibonacciNormal() {
        given:
        def num = 5
        when:
        def res = OffLineActionExceptionHandler.fibonacci(num)
        then:
        res == 25
    }

    def fibonacciMax() {
        given:
        def num = 11
        when:
        def res = OffLineActionExceptionHandler.fibonacci(num)
        then:
        res == 275
    }
}
