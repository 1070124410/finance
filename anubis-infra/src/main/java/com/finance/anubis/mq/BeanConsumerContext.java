package com.finance.anubis.mq;

public class BeanConsumerContext {
    private String topic;
    private String[] tags;
    private String group;
    private Object bean;
    private String messageType;
    private String messageMode;

    public BeanConsumerContext(String topic, String[] tags, String group, Object bean) {
        this.topic = topic;
        this.tags = tags;
        this.group = group;
        this.bean = bean;
    }

    // Getter and Setter methods for all fields

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessageMode(String messageMode) {
        this.messageMode = messageMode;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    // Additional methods if needed
}
