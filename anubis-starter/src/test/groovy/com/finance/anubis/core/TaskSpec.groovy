package com.finance.anubis.core
//package com.guming.finance.anubis.core
//
//import cn.hutool.extra.spring.EnableSpringUtil
//import com.aliyun.openservices.ons.api.PropertyValueConst
//import com.guming.finance.anubis.Application
//import com.guming.finance.anubis.core.config.EventResourceConfig
//import com.guming.finance.anubis.core.config.OnLineTaskConfig
//import com.guming.finance.anubis.core.config.URLResourceConfig
//import com.guming.finance.anubis.core.constants.enums.MessageInfraType
//import com.guming.finance.anubis.core.constants.enums.ResourceType
//import com.guming.finance.anubis.core.constants.enums.TaskStatus
//import com.guming.finance.anubis.core.context.ActivityContext
//import com.guming.finance.anubis.core.task.model.Task
//import com.guming.finance.anubis.repository.TaskRepository
//import com.guming.mq.annotation.EnableMQ
//import com.guming.mq.config.MQBaseAutoConfiguration
//import conf.BaseSpecification
//import feign.Request
//import org.junit.Test
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.FilterType
//import org.springframework.context.annotation.Import
//import org.springframework.test.annotation.DirtiesContext
//
//import javax.annotation.Resource
//
//@ComponentScan(basePackages = "com.guming.finance.anubis",
//        excludeFilters = [@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [Application.class])])
//@EnableSpringUtil
//@DirtiesContext
//@EnableMQ
//@Import(MQBaseAutoConfiguration.class)
//@SpringBootTest(properties = "spring.cloud.nacos.config.enabled=true", classes = [conf.CommonConfiguration])
//class TaskSpec extends BaseSpecification {
//
//    @Resource
//    private TaskRepository taskRepository
//
//
//
//    @Test
//    def "test_create_task"() {
//        given: "设置请求参数"
//        def sourceDataMapping = new HashMap<String, String>();
//        sourceDataMapping.put("orderId", '$.orderId');
//        sourceDataMapping.put("orderStatus", '$.orderStatus');
//        sourceDataMapping.put("payStatus", '$.payStatus');
//        Map<String, ActivityContext.ParamPath> requestParamMapping = new HashMap<>();
//        requestParamMapping.put("orderId", new ActivityContext.ParamPath("116_ORDER_EVENT", '$.orderId'));
//        Map<String, String> responseDatMap = new HashMap<>();
//        responseDatMap.put("payTime", '$.orderPayData.payTime');
//
//        def getTransTimeResourceConfig = new URLResourceConfig("http://172.22.93.220:8080/platform/order/core/read/queryOrderById/v1", Request.HttpMethod.GET.name(),requestParamMapping,
//                responseDatMap, ResourceType.URLResourceConfig, "GET_TRANS_TIME"
//        )
//        def sourceConfig = new EventResourceConfig("GID_CONSIST_GROUP",
//                "live_order_core_change_116",
//                Arrays.asList("citic_file_result", "liq_success_unfreeze_balance",
//                        "byte_dance_settle_tag", "byte_dance_withdraw",
//                        "byte_dance_withdraw_result", "liq_payable_sync_tag",
//                        "liq_payable_failed_tag"),
//                MessageInfraType.RocketMq,
//                PropertyValueConst.CLUSTERING,
//                sourceDataMapping,
//                "116_ORDER_EVENT",
//                Arrays.asList(getTransTimeResourceConfig)
//        )
//
//        Map<String, ActivityContext.ParamPath> promotionRequestParamMapping = new HashMap<>();
//        requestParamMapping.put("orderId", new ActivityContext.ParamPath("116_ORDER_EVENT", '$.orderId'));
//
//
//        Map<String, String> promotionResponseDataMap = new HashMap<>();
//        responseDatMap.put("payTime", '$.orderPayData.payTime');
//        def targetResourceConfig = new URLResourceConfig("http://172.22.93.220:8080/platform/order/core/read/queryOrderById/v1",
//                Request.HttpMethod.GET.name(), promotionRequestParamMapping,
//                promotionResponseDataMap, ResourceType.URLResourceConfig, "GET_TRANS_TIME"
//        )
//        def onLineTaskConfig = new OnLineTaskConfig(
//                sourceConfig,
//                Arrays.asList(targetResourceConfig),
//                Arrays.asList("orderId"),
//                10,
//                Arrays.asList('orderId'),
//                "couponCompare", null, null);
//        def task = new Task(onLineTaskConfig, TaskStatus.STOP);
//        taskRepository.add(task);
//    }
//
//
//    @Test
//    def "run_task"() {
//        given: "设置请求参数"
//        def task = taskRepository.selectById(1L);
//        when: "执行任务"
//        task.start()
//        then: "验证状态"
//        task.getStatus()==TaskStatus.START
//
//    }
//}
