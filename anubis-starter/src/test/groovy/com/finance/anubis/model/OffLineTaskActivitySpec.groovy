package com.finance.anubis.model


import com.guming.api.json.JsonUtil
import com.finance.anubis.core.config.OffLineResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.constants.enums.ActionResult
import com.finance.anubis.core.constants.enums.OffLineActivityResultType
import com.finance.anubis.core.constants.enums.OrderVerifyResultEnum
import com.finance.anubis.core.context.OffLineActivityContext
import com.finance.anubis.core.task.model.OffLineActivityResult
import com.finance.anubis.report.common.BaseSpecification
/**
 * @Author yezhaoyang
 * @Date 2023/03/15 10:04
 * @Description
 * */
class OffLineTaskActivitySpec extends BaseSpecification {

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
        taskActivity.setTaskConfig(config)
        taskActivity.setId(1)
    }

    def init() {
        given:
        when:
        print(com.finance.anubis.core.task.model.OffLineTaskActivity.init())
        then:
        true
    }

    def getComparator() {
        given:
        def key = "source"
        when:
        print(taskActivity.getComparator(key))
        then:
        true
    }

    def getResouceConfig() {
        given:
        def key = "source"
        when:
        print(taskActivity.getResouceConfig(key))
        then:
        true
    }

    def getResouceConfigTarget() {
        given:
        def key = "target"
        when:
        print(taskActivity.getResouceConfig(key))
        then:
        true
    }

    def getSourceContext() {
        given:
        def key = "source"
        when:
        print(taskActivity.getSourceContext(key))
        then:
        true
    }

    def getBizKey() {
        given:

        when:
        def res = com.finance.anubis.core.task.model.OffLineTaskActivity.getBizKey(taskActivity.getTaskConfig().getName(), taskActivity.getContext().getVerifyDate().toString())
        then:
        print(res)
    }

    def getBizKeyNull() {
        given:

        when:
        def res = com.finance.anubis.core.task.model.OffLineTaskActivity.getBizKey(taskActivity.getTaskConfig().getName(), "")
        then:
        res == null
    }

    def getBizKeyOrigin() {
        given:

        when:
        def res = taskActivity.getBizKey()
        then:
        print(res)
    }

    def prepareRequestParams() {
        given:
        def key = "source"
        when:
        taskActivity.prepareRequestParams(key)
        then:
        print(taskActivity.getSourceContext(key).getRequestParamMapping())
    }

    def prepareRequestParamsNull() {
        given:
        def key = "source"
        taskActivity.getSourceContext(key).setRequestParams(null)
        when:
        taskActivity.prepareRequestParams(key)
        then:
        print(taskActivity.getSourceContext(key).getRequestParamMapping())
    }

    def verifyNumericalTotal() {
        given:

        when:
        def res = taskActivity.verifyNumericalTotal()
        then:
        print(res)
    }

    def verifyNumericalTotalNull() {
        given:
        taskActivity.getSourceContext("source").setTotalAmount(null)
        taskActivity.getSourceContext("target").setTotalAmount(null)
        when:
        def res = taskActivity.verifyNumericalTotal()
        then:
        print(res)
    }

    def verifyNumericalTotalOneNull() {
        given:
        taskActivity.getSourceContext("source").setTotalAmount(null)
        when:
        def res = taskActivity.verifyNumericalTotal()
        then:
        print(res)
    }

    def verifyNumericalTotalAccord() {
        given:
        taskActivity.getSourceContext("source").setTotalAmount(new BigDecimal(BigInteger.ZERO))
        taskActivity.getSourceContext("target").setTotalAmount(new BigDecimal(BigInteger.ZERO))
        when:
        def res = taskActivity.verifyNumericalTotal()
        then:
        print(res)
    }

    def verifyNumericalTotalVARIANCE() {
        given:

        when:
        def res = taskActivity.verifyNumericalTotal()
        then:
        print(res)
    }

    def verifyFileDetail() {
        given:
        File directory = new File("")
        def path = directory.getAbsolutePath() + "\\src\\test\\groovy\\com\\guming\\finance\\anubis\\model"
        when:
        def res = taskActivity.verifyFileDetail(path + "\\2023-03-08_douYin_local_alipay", path + "\\2023-03-08_douYin_out_alipay")
        then:
        print(res)
    }

    def verifyFileDetailVariance() {
        given:
        File directory = new File("")
        def path = directory.getAbsolutePath() + "\\src\\test\\groovy\\com\\guming\\finance\\anubis\\model"
        when:
        def res = taskActivity.verifyFileDetail(path + "\\2023-03-08_douYin_local_wx", path + "\\2023-03-08_douYin_out_wx")
        then:
        print(res)
    }

    def prepareUniqueKeyLoc() {
        given:
        def key = "source"
        when:
        def res = taskActivity.prepareUniqueKeyLoc(taskActivity.getTaskConfig().getUniqueKeys(), taskActivity.getSourceContext(key).getFileHeader())
        then:
        print(res)
    }


    def prepareCompareKeyLoc() {
        given:
        def key = "source"
        when:
        def res = taskActivity.prepareCompareKeyLoc(taskActivity.getTaskConfig().getSourceConfig().getCompareDetailKeys(), taskActivity.getSourceContext(key).getFileHeader())
        then:
        print(res)
    }

    def doVerify() {
        given:
        def uniqueSourceKeyLoc = taskActivity.prepareUniqueKeyLoc(taskActivity.getTaskConfig().getUniqueKeys(), taskActivity.getSourceContext("source").getFileHeader())
        def compareSourceKeyLoc = taskActivity.prepareCompareKeyLoc(taskActivity.getTaskConfig().getSourceConfig().getCompareDetailKeys(), taskActivity.getSourceContext("source").getFileHeader())
        def uniqueTargetKeyLoc = taskActivity.prepareUniqueKeyLoc(taskActivity.getTaskConfig().getUniqueKeys(), taskActivity.getSourceContext("target").getFileHeader())
        def compareTargetKeyLoc = taskActivity.prepareCompareKeyLoc(taskActivity.getTaskConfig().getTargetConfig().getCompareDetailKeys(), taskActivity.getSourceContext("target").getFileHeader())
        when:
        def verify1 = taskActivity.doVerify("20230318187615540,4,1000,1,0", "20230318187615541,4,1000,1,0", uniqueSourceKeyLoc, compareSourceKeyLoc, uniqueTargetKeyLoc, compareTargetKeyLoc)
        def verify2 = taskActivity.doVerify("20230318187615541,4,1000,1,0", "20230318187615540,4,1000,1,0", uniqueSourceKeyLoc, compareSourceKeyLoc, uniqueTargetKeyLoc, compareTargetKeyLoc)
        def verify3 = taskActivity.doVerify("20230318187615541,4,1000,2,1", "20230318187615541,4,1000,2,0", uniqueSourceKeyLoc, compareSourceKeyLoc, uniqueTargetKeyLoc, compareTargetKeyLoc)
        then:
        OrderVerifyResultEnum.SOURCE_UNIQUE == verify1
        OrderVerifyResultEnum.TARGET_UNIQUE == verify2
        OrderVerifyResultEnum.COINCIDENT == verify3
    }

    def buildUniqueKey() {
        given:
        def key = "source"
        def uniqueSourceKeyLoc = taskActivity.prepareUniqueKeyLoc(taskActivity.getTaskConfig().getUniqueKeys(), taskActivity.getSourceContext(key).getFileHeader());
        String line = "20230318187615541,4,1000,2,1"
        when:
        def res = taskActivity.buildUniqueKey(uniqueSourceKeyLoc, line)
        then:
        print(res)
    }

    def getTotalResult() {
        given:
        def resultInfo = new OffLineActivityResult.TotalResultInfo()
        resultInfo.setResultInfoType(OffLineActivityResultType.TOTAL)
        resultInfo.setSourceTotal(new BigDecimal(BigInteger.ZERO))
        resultInfo.setTargetTotal(new BigDecimal(BigInteger.ZERO))
        def result = ActionResult.ACCORD
        when:
        def res = taskActivity.getTotalResult(resultInfo, result)
        then:
        print(res)
    }

    def getDetailResult(){
        given:
        def resultInfo = new OffLineActivityResult.DetailResultInfo()
        resultInfo.setResultInfoType(OffLineActivityResultType.DETAIL)
        resultInfo.setInConsistency(new HashSet<String>())
        resultInfo.setSourceUnique(new HashSet<String>())
        resultInfo.setTargetUnique(new HashSet<String>())
        def result = ActionResult.ACCORD
        when:
        def res = taskActivity.getDetailResult(resultInfo, result)
        then:
        print(res)
    }

    def getAbsoluteFilePath() {
        given:
        when:
        def res = taskActivity.getAbsoluteFilePath()
        then:
        print(res)
    }

    def initSourceContext() {
        given:
        taskActivity.getResouceConfig("target").setResourceType("OffLineHttpResource")
        when:
        taskActivity.initSourceContext()
        then:
        println(taskActivity.getSourceContext("source"))
        println(taskActivity.getSourceContext("target"))
    }


}
