package com.finance.anubis.task.executor

import cn.hutool.extra.spring.SpringUtil
import cn.hutool.json.JSONUtil
import com.guming.api.json.JsonUtil
import com.finance.anubis.core.config.OffLineFileResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.factory.FileParserUtilFactory
import com.finance.anubis.core.task.executor.FileFetchExecutor
import com.finance.anubis.core.util.OSSUtil
import com.finance.anubis.report.common.BaseSpecification
import mockit.Mock
import mockit.MockUp
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestTemplate

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 13:48
 * @Description
 * */
@Import([SpringUtil, FileFetchExecutor,com.finance.anubis.core.util.FileUtil,RestTemplate])
class FileFetchExecutorSpec extends BaseSpecification {

    @Autowired
    FileFetchExecutor executor

    @Autowired
    com.finance.anubis.core.util.FileUtil fileUtil

    @Autowired
    RestTemplate restTemplate
    @SpringBean
    OSSUtil ossUtil = Mock()
    @SpringBean
    com.finance.anubis.core.util.ExternalSortUtil externalSortUtil = Mock()
    @SpringBean
    FileParserUtilFactory factory=Mock()
    def taskActivity

    def setup() {
//        fileUtil.createFile(_) >> true
//        fileUtil.streamDownload(_, _, _, _) >> 1
        new MockUp<BufferedReader>() {
            @Mock
            String exists() {
                return "9.9"
            }
        }
        taskActivity = new com.finance.anubis.core.task.model.OffLineTaskActivity()

        def config = new OffLineTaskConfig()
        String sourceJson = "{\"key\":\"source\",\"url\":\"http://172.22.80.64:8080/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000}"
        def source = JsonUtil.of(sourceJson, OffLineFileResourceConfig.class)
        String targetJson = "{\"key\":\"target\",\"url\":\"http://172.22.80.64:8080/verifyTaskFile/downloadDouYinVerifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":100000}"
        def target = JsonUtil.of(targetJson, OffLineFileResourceConfig.class)
        config.setSourceConfig(source)
        config.setTargetConfig(target)
        config.setUniqueKeys(Collections.singletonList("orderId"))
        config.setErrorThreshold(1000)
        taskActivity.setTaskConfig(config)
        taskActivity.initSourceContext()
        taskActivity.getSourceContext("source").setRequestParams("{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994298303&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=60cXZ7apeCXkA2GUoIEYvONVhCY%3D\"}")
        taskActivity.getSourceContext("target").setRequestParams("{\"fileUrl\":\"http://gm-file-bucket.oss-cn-hangzhou.aliyuncs.com/liq-service-test/verify/2023-03-08_douYin_out_alipay?Expires=1994298303&OSSAccessKeyId=LTAI4GHziWj4PTNcbQzX8XA3&Signature=60cXZ7apeCXkA2GUoIEYvONVhCY%3D\"}")
        taskActivity.getSourceContext("source").setCustom("{\"date\":\"2023-03-08\",\"payChannel\":4}")
        taskActivity.getSourceContext("target").setCustom("{\"date\":\"2023-03-08\",\"payChannel\":4}")
        taskActivity.prepareRequestParams("source")
        taskActivity.prepareRequestParams("target")
        taskActivity.getContext().setTaskName("DOU_YIN_ORDER_ALIPAY")
    }


    def "dealWithLine"() {
        def str = "{\"id\":1,\"price\":60,\"times\":1}"
        def parse = JSONUtil.parse(str)

    }

    def "prepareData"() {
//        given:
//
//        when:
//        def executorService = Executors.newFixedThreadPool(2)
//        executorService.execute({ -> executor.prepareData(taskActivity, "source") })
//        executorService.execute({ -> executor.prepareData(taskActivity, "target") })
//        sleep(10000)
//        then:
//        print(JsonUtil.toJson(taskActivity))

    }

}
