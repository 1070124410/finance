package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum MessageInfraType implements EnumBase {
    Kafka("Kafka", "Kafka"), RocketMq("RocketMq", "RocketMq"), Ons("Ons", "Ons"),
    ;
    @Getter
    private final String code;
    @Getter
    private final String message;

    public static MessageInfraType of(String code) {
        return Arrays.stream(MessageInfraType.values()).filter(messageInfraType -> messageInfraType.code.equals(code)).findFirst().orElse(null);
    }

}
