package com.finance.anubis.mq;

public class DecorateUtils {
    private static final String GROUP_PREFIX = "group_";
    private static final String TOPIC_PREFIX = "topic_";

    public static String decorateGroup(String group) {
        return GROUP_PREFIX + group;
    }

    public static String decorateTopic(String topic) {
        return TOPIC_PREFIX + topic;
    }
}
