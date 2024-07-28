package com.guming.finance.mq

import cn.hutool.extra.spring.SpringUtil
import com.aliyun.openservices.ons.api.Action
import com.aliyun.openservices.ons.api.ConsumeContext
import com.aliyun.openservices.ons.api.Message
import com.finance.anubis.core.constants.Constants
import com.guming.api.json.JsonUtil
import com.finance.anubis.core.config.OffLineResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.constants.enums.OffLineAction
import com.finance.anubis.core.consumer.OffLineTaskActivityActionConsumer
import com.finance.anubis.core.context.OffLineActivityContext
import com.finance.anubis.core.factory.OffLineActionHandlerFactory
import com.finance.anubis.core.task.model.OffLineTaskActivity
import com.finance.anubis.core.task.stage.offlineHandler.DataFetchActionHandler
import com.finance.anubis.repository.OffLineTaskActivityRepository
import com.guming.finance.report.common.BaseSpecification
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

import java.time.LocalDateTime

/**
 * @Author yezhaoyang
 * @Date 2023/03/14 17:51
 * @Description
 * */
@Import([SpringUtil, OffLineTaskActivityActionConsumer])
class OffLineTaskActivityActionConsumerSpec extends BaseSpecification {

    @Autowired
    OffLineTaskActivityActionConsumer consumer

    @SpringBean
    OffLineActionHandlerFactory actionHandlerFactory = Mock()
    @SpringBean
    OffLineTaskActivityRepository activityRepository = Mock()

    def taskActivity

    def setup() {
        def sourceConfig = "{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100}"
        def targetConfig = "{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100}"
        def taskConfig = new OffLineTaskConfig()
        taskConfig.setSourceConfig(JsonUtil.of(sourceConfig, OffLineResourceConfig.class))
        taskConfig.setTargetConfig(JsonUtil.of(targetConfig, OffLineResourceConfig.class))
        taskConfig.setId(1L)
        taskConfig.setName("DOU_YIN_ORDER_ALIPAY")
        taskActivity = OffLineTaskActivity.init()
        taskActivity.setTaskConfig(taskConfig)
        taskActivity.initSourceContext()
        String sourceContextJson = "{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\"},\"pathList\":[],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"totalAmount\":0,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.256\"}"
        taskActivity.getContext().getSourceContext().put("source", JsonUtil.of(sourceContextJson, OffLineActivityContext.SourceContext.class))
        def targetContextJson = "{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"totalAmount\":3000,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.291\"}"
        taskActivity.getContext().getSourceContext().put("target", JsonUtil.of(targetContextJson, OffLineActivityContext.SourceContext.class))
        taskActivity.setId(1L)
        taskActivity.setVerifyDate(LocalDateTime.now().toString())
    }

    def "testConsume"() {
        given:
        def msg = "{\"configKey\":\"target\",\"action\":\"DATA_FETCH\",\"taskName\":\"DOU_YIN_ORDER_ALIPAY\"}"
        Message ms = new Message(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC, Constants.OFFLINE_TASK_CONSUMER_GROUP, null, msg.getBytes())
        taskActivity.setAction(OffLineAction.DATA_FETCH)
        activityRepository.getByBizKey(_) >> taskActivity
        DataFetchActionHandler handler = Mock()
        actionHandlerFactory.getOffLineHandler(_) >> handler
        when:
        def res = consumer.consume(ms, new ConsumeContext())
        then:
        res == Action.CommitMessage
    }


    def "testConsumeNullParam"() {
        given:
        def msg = "{\"configKey\":\"target\",\"action\":\"DATA_FETCH\"}"
        Message ms = new Message(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC, Constants.OFFLINE_TASK_CONSUMER_GROUP, null, msg.getBytes())
        when:
        def res = consumer.consume(ms, new ConsumeContext())
        then:
        res == Action.ReconsumeLater
    }
}
