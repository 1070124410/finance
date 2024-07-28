package com.finance.anubis.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.constants.MessageModeConst;
import com.finance.anubis.enums.MessageInfraType;
import com.finance.anubis.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/***
 *
 * @author longchuan
 * @date 2022年12月28日18:13:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "resourceType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = EventResourceConfig.class,
                name = "EventResourceConfig")})
public class MessageResourceConfig extends ResourceConfig {

    // 消息监听组
    private String group;
    // 消息监听topic
    private String topic;
    //消息监听tags
    private List<String> tags;
    // 消息监听模式 默认为集群模式
    private String messageMode = MessageModeConst.CLUSTERING;
    // 消息基件类型，目前支持ons、rocketmq
    private MessageInfraType messageInfraType;

    public MessageResourceConfig(
            String group,
            String topic,
            List<String> tags,
            MessageInfraType messageInfraType,
            String messageMode,
            Map<String, String> dataMapping,
            ResourceType resourceType,
            String key
    ) {
        super(resourceType, dataMapping, key);
        this.group = group;
        this.topic = topic;
        this.tags = tags;
        this.messageMode = messageMode;
        this.messageInfraType = messageInfraType;
    }

}
