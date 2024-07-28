package com.finance.anubis.stage.offlineHandler

import cn.hutool.extra.spring.SpringUtil
import com.aliyun.openservices.ons.api.SendResult
import com.guming.fd.distributed.lock.DistributedLock
import com.guming.fd.distributed.lock.DistributedLockProvider
import com.finance.anubis.core.config.OffLineFileResourceConfig
import com.finance.anubis.core.config.OffLineTaskConfig
import com.finance.anubis.core.constants.enums.OffLineAction
import com.finance.anubis.core.constants.enums.SourceDataStatus
import com.finance.anubis.core.constants.enums.StatusResult
import com.finance.anubis.core.context.OffLineActivityContext
import com.finance.anubis.core.factory.PrepareDataExecutorFactory
import com.finance.anubis.core.task.executor.FileFetchExecutor
import com.finance.anubis.report.common.BaseSpecification
import com.guming.mq.api.MessageProducer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

import java.time.LocalDateTime
/**
 * @Author yezhaoyang
 * @Date 2023/03/15 15:57
 * @Description
 * */
@Import([SpringUtil, com.finance.anubis.core.task.stage.offlineHandler.DataFetchActionHandler])
class DataFetchActionHandlerTest extends BaseSpecification {
    @SpringBean
    com.finance.anubis.repository.OffLineTaskActivityRepository taskActivityRepository = Mock()
    @SpringBean
    MessageProducer messageProducer = Mock()
    @SpringBean
    DistributedLockProvider lockProvider = Mock()
    @SpringBean
    PrepareDataExecutorFactory prepareDataExecutorFactory = Mock()
    @SpringBean
    FileFetchExecutor fetchExecutor = Mock()

    @Autowired
    com.finance.anubis.core.task.stage.offlineHandler.DataFetchActionHandler handler
    def taskActivity

    def setup() {
        taskActivity = new com.finance.anubis.core.task.model.OffLineTaskActivity()
        def context = OffLineActivityContext.initActivityContext()
        context.getSourceContext().put("source", new OffLineActivityContext.FileSourceContext())
        context.getSourceContext().get("source").setFileHeader(Collections.singletonList("orderId"))
        context.getSourceContext().get("source").setTotalAmount(new BigDecimal("9.9"))
        context.getSourceContext().put("target", new OffLineActivityContext.FileSourceContext())
        context.getSourceContext().get("target").setFileHeader(Collections.singletonList("orderId"))
        context.getSourceContext().get("target").setTotalAmount(null)
        context.setVerifyDate(LocalDateTime.now())
        taskActivity.setContext(context)
        def config = new OffLineTaskConfig()
        def source = new OffLineFileResourceConfig()
        source.setKey("source")
        source.setResourceType("OffLineFileResource")
        source.setCompareDetailKeys(Collections.singletonList("orderId"))
        def target = new OffLineFileResourceConfig()
        target.setKey("target")
        target.setResourceType("OffLineHttpResource")
        target.setCompareDetailKeys(Collections.singletonList("orderId"))
        def mapping = new HashMap()
        mapping.put("orderId", "1")
        target.setRequestParamMapping(mapping)
        def paramMap = new HashMap<String, String>()
        paramMap.put("orderId", "\$.orderId")
        source.setRequestParamMapping(paramMap)
        def map = new LinkedHashMap<>()
        map.put("orderId", "ASC")
        source.setSortKeyMap(map)
        config.setSourceConfig(source)
        config.setTargetConfig(target)
        config.setUniqueKeys(Collections.singletonList("orderId"))
        config.setErrorThreshold(1000)
        taskActivity.setTaskConfig(config)
    }

    def "CheckStatusNull"() {
        given:

        when:
        def res = handler.checkStatus(taskActivity, "source")
        then:
        res == StatusResult.ACT
    }

    def "CheckStatusNotReady"() {
        given:
        taskActivity.getSourceContext("source").setSourceDataStatus(SourceDataStatus.DATA_READY)
        when:
        def res = handler.checkStatus(taskActivity, "source")
        then:
        res == StatusResult.IGNORE
    }

    def "CheckStatusReady"() {
        given:
        taskActivity.getSourceContext("source").setSourceDataStatus(SourceDataStatus.DATA_READY)
        taskActivity.getSourceContext("target").setSourceDataStatus(SourceDataStatus.DATA_READY)
        when:
        def res = handler.checkStatus(taskActivity, "source")
        then:
        res == StatusResult.ONLY_PUSH
    }

    def "InnerHandle"() {
        given:
        prepareDataExecutorFactory.getPrepareDataExecutor(_) >> fetchExecutor
        when:
        handler.innerHandle(taskActivity, "source")
        then:
        true
    }

    def "UpdateActivity"() {
        given:
        taskActivityRepository.toDataCompareTotal(_) >> true
        taskActivity.getSourceContext("source").setSourceDataStatus(SourceDataStatus.DATA_READY)
        taskActivity.getSourceContext("target").setSourceDataStatus(SourceDataStatus.DATA_READY)
        def lock = Mock(DistributedLock)
        lock.tryApplyWithinLockScopeInterruptibly(_, _, _) >> true
        lockProvider.getOrCreate(_) >> lock
        taskActivityRepository.getByBizKey(_) >> taskActivity
        when:
        handler.updateActivity(taskActivity, "source")
        then:
        true
    }

    def "AfterHandle"() {
        given:
        taskActivity.setAction(OffLineAction.DATA_COMPARE_TOTAL)
        messageProducer.syncSend(_) >> new SendResult()
        when:
        handler.afterHandle(taskActivity, "source")
        then:
        true
    }
}
