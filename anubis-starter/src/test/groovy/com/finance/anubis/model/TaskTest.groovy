package com.finance.anubis.model


import com.guming.finance.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 14:55
 * @Description
 * */
class TaskTest extends BaseSpecification {
    com.finance.anubis.core.task.model.Task task
    void setup() {
        task=Mock()
    }

    def "ListenStart"() {
        given:
        when:
        task.listenStart()
        then:
        true
    }

    def "ListenStop"() {
        given:
        when:
        task.listenStop()
        then:
        true
    }

    def "Start"() {
        given:
        when:
        task.start()
        then:
        true
    }

    def "Stop"() {
        given:
        when:
        task.stop()
        then:
        true
    }
}
