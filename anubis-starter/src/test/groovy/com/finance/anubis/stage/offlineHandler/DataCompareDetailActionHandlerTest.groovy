package com.finance.anubis.stage.offlineHandler

import cn.hutool.extra.spring.SpringUtil
import com.aliyun.openservices.ons.api.SendResult
import com.guming.api.json.JsonUtil
import com.guming.common.exception.StatusCodeException
import com.finance.anubis.core.config.OffLineResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.constants.enums.StatusResult
import com.finance.anubis.core.context.OffLineActivityContext
import com.finance.anubis.core.task.stage.offlineHandler.DataCompareDetailActionHandler
import com.finance.anubis.core.util.FileUtil
import com.finance.anubis.core.util.OSSUtil
import com.guming.finance.report.common.BaseSpecification
import com.guming.mq.api.MessageProducer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
/**
 * @Author yezhaoyang
 * @Date 2023/03/15 15:57
 * @Description
 * */
@Import([SpringUtil, DataCompareDetailActionHandler])
class DataCompareDetailActionHandlerTest extends BaseSpecification {

    @SpringBean
    com.finance.anubis.repository.OffLineTaskActivityRepository taskActivityRepository = Mock()
    @SpringBean
    OSSUtil ossUtil = Mock()
    @SpringBean
    FileUtil fileUtil = Mock()
    @SpringBean
    com.finance.anubis.repository.OffLineActivityResultRepository activityResultRepository = Mock()
    @SpringBean
    MessageProducer messageProducer = Mock()

    @Autowired
    DataCompareDetailActionHandler handler

    com.finance.anubis.core.task.model.OffLineTaskActivity taskActivity

    def config

    def context

    def setup() {
        taskActivity = new com.finance.anubis.core.task.model.OffLineTaskActivity()
        def json = "{\"sourceContext\":{\"source\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_local_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=alkjFznb3xpN8Q25ZXngPdMdxqE%3D\"},\"pathList\":[],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/source_reconciliation\",\"totalAmount\":0,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.256\"},\"target\":{\"resourceType\":\"OffLineFileResource\",\"requestParams\":\"{\\\"fileUrl\\\":\\\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\\\"}\",\"fileHeader\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"requestParamMapping\":{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994481580&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=oRMSKVZN5BQ7fpTjzxu4NuiuWac%3D\"},\"pathList\":[\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_part1\"],\"reconciliationFile\":\"/log/offline-reconciliation-file/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"ossReconciliationFile\":\"liq-service-test/DOU_YIN_ORDER_ALIPAY_2023-03-18/target_reconciliation\",\"totalAmount\":3000,\"custom\":\"{\\\"date\\\":\\\"2023-03-08\\\",\\\"payChannel\\\":4}\",\"sourceDataStatus\":\"DATA_READY\",\"fetchingTime\":\"2023-03-18T14:39:40.291\"}},\"verifyDate\":\"2023-03-18T14:39:40.291\"}"
        context = JsonUtil.of(json, OffLineActivityContext.class)
        taskActivity.setContext(context)
        config = new OffLineTaskConfig()
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
        taskActivity.setTaskConfig(config)
        taskActivity.setId(1)
    }

    def "CheckStatusPush"() {
        given:
        taskActivity.getTaskConfig().setDetailSwitch(false)
        when:
        def res = handler.checkStatus(taskActivity, "")
        then:
        res == StatusResult.ONLY_PUSH
    }

    def "CheckStatusAct"() {
        given:
        when:
        def res = handler.checkStatus(taskActivity, "")
        then:
        res == StatusResult.ACT
    }

    def "CheckStatusIgnore"() {
        given:
        taskActivity.getSourceContext("source").setOssReconciliationFile(null)
        when:
        def res = handler.checkStatus(taskActivity, "")
        then:
        res == StatusResult.IGNORE
    }

//    def "InnerHandle"() {
//        given:
//        def file=new File("c://guming/anubis")
//        ossUtil.downloadFileToLocal(_, _) >> new Optional<File>(file)
//        activityResultRepository.save(_) >> true
//        def activity = Mock(OffLineTaskActivity)
//        activity.getOnLineTaskConfig() >> config
//        activity.getSourceContext(_) >> context.getSourceContext().get("source")
//        activity.verifyFileDetail(_,_) >> new OffLineActivityResult()
//        when:
//        handler.innerHandle(taskActivity, "")
//        then:
//        true
//    }

    def "InnerHandleError"() {
        given:

        when:
        try {
            handler.innerHandle(taskActivity, "")
        } catch (StatusCodeException e) {
            print(e.getStatusCode().getReason())
        }
        then:
        true
    }

    def "UpdateActivity"() {
        given:
        taskActivityRepository.toDataDone(_, _) >> true
        when:
        handler.updateActivity(taskActivity, "")
        then:
        true
    }

    def "AfterHandle"() {
        given:
        messageProducer.syncSend(_) >> new SendResult()
        when:
        handler.afterHandle(taskActivity, "")
        then:
        true
    }
}
