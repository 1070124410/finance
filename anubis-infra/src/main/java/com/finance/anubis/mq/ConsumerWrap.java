package com.finance.anubis.mq;

import org.apache.rocketmq.client.consumer.listener.MessageListener;

import java.util.concurrent.ConcurrentHashMap;

public class ConsumerWrap {
    private ConcurrentHashMap<String, MessageListener> subscribeTable;
    private Consumer consumer;

    public ConsumerWrap(Consumer consumer) {
        this.consumer = consumer;
        this.subscribeTable = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, MessageListener> getSubscribeTable() {
        return subscribeTable;
    }

    public void setSubscribeTable(ConcurrentHashMap<String, MessageListener> subscribeTable) {
        this.subscribeTable = subscribeTable;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}