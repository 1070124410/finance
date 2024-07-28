package com.finance.anubis.mq;


import java.util.concurrent.ConcurrentHashMap;

public class ConsumerHolder {
    private ConcurrentHashMap<String, ConsumerWrap> consumers = new ConcurrentHashMap<>();

    public void addConsumer(String groupId, ConsumerWrap consumerWrap) {
        consumers.put(groupId, consumerWrap);
    }

    public ConsumerWrap getConsumer(String groupId) {
        return consumers.get(groupId);
    }

    // Other methods as required
}


