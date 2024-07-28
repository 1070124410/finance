package com.finance.anubis.service
//package com.guming.finance.anubis.service
//
//import cn.hutool.extra.spring.SpringUtil
//import com.guming.common.redis.RedisClient
//import com.guming.finance.anubis.core.config.OffLineFileResourceConfig
//import com.guming.finance.anubis.core.config.OffLineTaskConfig
//import com.guming.finance.anubis.core.context.OffLineActivityContext
//import com.guming.finance.anubis.core.job.OffLineTaskStartJobHandler
//import com.guming.finance.anubis.core.task.model.OffLineTaskActivity
//import com.guming.finance.anubis.core.task.model.Task
//import com.guming.finance.anubis.dto.OffLineDataReadyDTO
//import com.guming.finance.anubis.repository.OffLineTaskActivityRepository
//import com.guming.finance.anubis.repository.TaskRepository
//import com.guming.finance.anubis.service.impl.OffLineTaskServiceImpl
//import com.guming.finance.report.common.BaseSpecification
//import com.guming.mq.api.MessageProducer
//import org.spockframework.spring.SpringBean
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Import
//
///**
// * @Author yezhaoyang
// * @Date 2023/03/15 17:46
// * @Description
// * */
//@Import([SpringUtil, OffLineTaskServiceImpl])
//class OffLineTaskServiceSpec extends BaseSpecification {
//
//
//    @SpringBean
//    TaskRepository taskRepository = Mock()
//    @SpringBean
//    MessageProducer messageProducer = Mock()
//    @SpringBean
//    OffLineTaskActivityRepository taskActivityRepository = Mock()
//    @SpringBean
//    RedisClient redisClient = Mock()
//
//    @Autowired
//    OffLineTaskService offLineTaskService
//
//    def taskActivity
//    def dto
//    def task
//
//    def setup() {
//        taskActivity = new OffLineTaskActivity()
//        def context = OffLineActivityContext.initActivityContext()
//        context.getSourceContext().put("source", new OffLineActivityContext.FileSourceContext())
//        context.getSourceContext().get("source").setFileHeader(Collections.singletonList("orderId"))
//        context.getSourceContext().get("source").setTotalAmount(new BigDecimal("9.9"))
//        context.getSourceContext().put("target", new OffLineActivityContext.FileSourceContext())
//        context.getSourceContext().get("target").setFileHeader(Collections.singletonList("orderId"))
//        context.getSourceContext().get("target").setTotalAmount(null)
//        context.setTaskName("DOU_YIN_ORDER")
//        taskActivity.setContext(context)
//        taskActivity.setTimes(1)
//        def config = new OffLineTaskConfig()
//        def source = new OffLineFileResourceConfig()
//        source.setKey("source")
//        source.setResourceType("OffLineFileResource")
//        source.setCompareDetailKeys(Collections.singletonList("orderId"))
//        def target = new OffLineFileResourceConfig()
//        target.setKey("target")
//        target.setResourceType("OffLineHttpResource")
//        target.setCompareDetailKeys(Collections.singletonList("orderId"))
//        def mapping = new HashMap()
//        mapping.put("orderId", "1")
//        target.setRequestParamMapping(mapping)
//        def paramMap = new HashMap<String, String>()
//        paramMap.put("orderId", "\$.orderId")
//        source.setRequestParamMapping(paramMap)
//        def map = new LinkedHashMap<>()
//        map.put("orderId", "ASC")
//        source.setSortKeyMap(map)
//        config.setSourceConfig(source)
//        config.setTargetConfig(target)
//        config.setUniqueKeys(Collections.singletonList("orderId"))
//        config.setErrorThreshold(1000)
//        taskActivity.setOnLineTaskConfig(config)
//
//        task=new Task()
//        task.setOffLineTaskConfig(config)
//
//        dto = new OffLineDataReadyDTO()
//        dto.setTaskName("task")
//        dto.setConfigKey("source")
//        dto.setCustom("")
//        dto.setRequestParam("{\"id\":1,\"price\":60,\"times\":1}")
//    }
//
//    def "dataReadyNull"() {
//        given:
//        taskActivityRepository.save(_) >> true
//        taskRepository.selectByName(_) >> task
//        redisClient.remove(_) >> null
//        redisClient.hmSet(_,_) >> true
//        messageProducer.syncSend(_) >> null
//        when:
//        offLineTaskService.dataReady(dto)
//        then:
//        true
//    }
//
//    def "dataReady"() {
//        given:
////        OffLineTaskStartJobHandler.taskActivityMap.put(dto.getTaskName(),taskActivity)
//        taskActivityRepository.getByBizKey(_) >> taskActivity
//        when:
//        offLineTaskService.dataReady(dto)
//        then:
//        true
//    }
//
//    def "construct"(){
//        given:
//        when:
//        def repository=new OffLineTaskServiceImpl(taskRepository,messageProducer,taskActivityRepository,redisClient)
//        then:
//        true
//    }
//
//}
