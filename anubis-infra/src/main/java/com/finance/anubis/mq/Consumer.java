package com.finance.anubis.mq;

public interface Consumer {
    void subscribe(String topicId);
    void unsubscribe(String topicId);
}
