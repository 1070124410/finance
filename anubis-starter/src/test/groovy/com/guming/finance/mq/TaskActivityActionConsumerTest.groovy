package com.finance.anubis.mq

import cn.hutool.extra.spring.SpringUtil
import com.aliyun.openservices.ons.api.Action
import com.aliyun.openservices.ons.api.ConsumeContext
import com.aliyun.openservices.ons.api.Message
import com.finance.anubis.core.consumer.TaskActivityActionConsumer
import com.finance.anubis.core.factory.ActionHandlerFactory
import com.finance.anubis.core.task.model.TaskActivity
import com.finance.anubis.core.task.stage.handler.SourceFetchActionHandler
import com.finance.anubis.repository.TaskActivityRepository
import com.finance.anubis.report.common.BaseSpecification
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 15:07
 * @Description
 * */
@Import([SpringUtil, TaskActivityActionConsumer])
class TaskActivityActionConsumerTest extends BaseSpecification {

    @Autowired
    TaskActivityActionConsumer actionConsumer
    @SpringBean
    TaskActivityRepository activityRepository = Mock()
    @SpringBean
    ActionHandlerFactory handlerFactory = Mock()

    def "consume"() {
        given:
        def message = new Message()
        message.setBody("{\"actionResult\":\"UNREADY\",\"taskActivityId\":15509}".getBytes())
        message.setTopic("")
        message.setTag("")
        message.setMsgID("1")
        when:
        actionConsumer.consume(message, new ConsumeContext())
        then:
        true
    }

    def "consumeDone"() {
        given:
        def message = new Message()
        message.setBody("{\"actionResult\":\"UNREADY\",\"taskActivityId\":15509}".getBytes())
        message.setTopic("")
        message.setTag("")
        message.setMsgID("1")
        def taskActivity = new TaskActivity()
        taskActivity.setAction(com.finance.anubis.core.constants.enums.Action.DONE)
        activityRepository.getById(_) >> taskActivity
        SourceFetchActionHandler handler = Mock()
        handlerFactory.getHandler(_) >> handler
        when:
        def res = actionConsumer.consume(message, new ConsumeContext())
        then:
        res== Action.CommitMessage
    }
}
