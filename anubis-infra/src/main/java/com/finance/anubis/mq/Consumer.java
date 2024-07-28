package com.finance.anubis.mq;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费方
 * 1.如果两个消费者group和topic都一样，则二者轮循接收消息
 * 2.如果两个消费者topic一样，而group不一样，则消息变成广播机制
 * RocketMQListener<>泛型必须和接收的消息类型相同
 */
@Component
@RocketMQMessageListener(
        topic = "sendMessage",                      // 1.topic：消息的发送者使用同一个topic
        consumerGroup = "test-group",               // 2.group：不用和生产者group相同 ( 在RocketMQ中消费者和发送者组没有关系 )
        selectorExpression = "*",                   // 3.tag：设置为 * 时，表示全部。
        messageModel = MessageModel.CLUSTERING    // 4.消费模式：默认 CLUSTERING （ CLUSTERING：负载均衡 ）（ BROADCASTING：广播机制 ）
)
public class Consumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String str) {
        System.out.println("收到消息" + str);
    }
}
