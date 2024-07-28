package com.finance.anubis.mq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class MessageProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 普通字符串消息
     */
    public void sendMessage(String msg) {
        rocketMQTemplate.convertAndSend("sendMessage", msg);
    }

    /**
     * 同步消息
     */
    public void syncSend() {
        String json = "同步消息";
        SendResult sendMessage = rocketMQTemplate.syncSend("sendMessage", json);
        System.out.println(sendMessage);
    }

    /**
     * 异步消息
     */
    public void asyncSend() {
        String json = "异步消息";
        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("123");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("456");
            }
        };
        rocketMQTemplate.asyncSend("sendMessage", json, callback);
    }

    /**
     * 单向消息
     */
    public void onewaySend() {
        String json = "单向消息";
        rocketMQTemplate.sendOneWay("sendMessage", json);
    }
}

