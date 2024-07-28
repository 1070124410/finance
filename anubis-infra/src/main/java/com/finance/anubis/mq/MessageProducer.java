package com.finance.anubis.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;
import org.apache.rocketmq.common.message.Message;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class MessageProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    private static final int[] DELAY_TIMES_MS = {
            1000,  // 1 sec
            1000 * 5,  // 5 sec
            1000 * 10,  // 10 sec
            1000 * 30,  // 30 sec
            1000 * 60,  // 1 min
            1000 * 60 * 2,  // 2 min
            1000 * 60 * 3,  // 3 min
            1000 * 60 * 4,  // 4 min
            1000 * 60 * 5,  // 5 min
            1000 * 60 * 10,  // 10 min
            1000 * 60 * 30,  // 30 min
            1000 * 60 * 60,  // 1 hour
            1000 * 60 * 60 * 2,  // 2 hours
            1000 * 60 * 60 * 4,  // 4 hours
            1000 * 60 * 60 * 8,  // 8 hours
            1000 * 60 * 60 * 12,  // 12 hours
            1000 * 60 * 60 * 24,  // 1 day
            1000 * 60 * 60 * 48  // 2 days (last level is actually around 15 days)
    };


    public Message buildMsg(String topic, String tag, String msg){
        return new Message(topic, tag, msg.getBytes());
    }
    /**
     * 同步消息
     */
    public void syncSend(String msg) {
        SendResult sendMessage = rocketMQTemplate.syncSend("sendMessage", msg);
        System.out.println(sendMessage);
    }

    /**
     * 同步发送消息到指定的topic。
     * @param topic 消息主题
     * @param tag 消息标签
     * @param msg 消息内容
     */
    public void syncSend(String topic, String tag, String msg) {
        Message message = buildMsg(topic, tag, msg);
        rocketMQTemplate.syncSend(topic, message);
    }

    /**
     * 异步消息
     */
    public void asyncSend(String topic, String tag, String msg) {
        Message message = buildMsg(topic, tag, msg);
        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("asyncSend success");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("asyncSend fail");
            }
        };
        rocketMQTemplate.asyncSend(topic, message, callback);
    }


    /**
     * 发送延迟消息
     */
    public void sendDelayMsg(String topic, String tag, Object body, int delayLevel) {
        try {
            Message message = buildMsg(topic, tag, body.toString());
            message.setDelayTimeLevel(delayLevel);

            // 使用 RocketMQTemplate 发送延迟消息
            rocketMQTemplate.getProducer().send(message);
        }  catch (RemotingException | MQClientException | MQBrokerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将延迟时间转换为RocketMQ的延迟等级。
     * @param delayTime 延迟时间
     * @param unit 时间单位
     * @return 延迟等级
     */
    public static int getRocketMQDelayLevel(int delayTime, TimeUnit unit) {
        long delayTimeMs = unit.toMillis(delayTime);
        int level = 1;
        for (int i = 0; i < DELAY_TIMES_MS.length; i++) {
            if (delayTimeMs <= DELAY_TIMES_MS[i]) {
                return level;
            }
            level++;
        }
        return 18; // 如果超过最大延迟时间，则返回最大等级
    }
}

