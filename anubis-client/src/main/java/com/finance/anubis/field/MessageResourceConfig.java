package com.finance.anubis.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:37
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
    private String group;
    private String topic;
    private List<String> tags;

    private String messageMode = PropertyValueConst.CLUSTERING;

    private String messageInfraType;


    public class PropertyValueConst {

        /**
         * 广播消费模式
         */
        public static final String BROADCASTING = "BROADCASTING";

        /**
         * 集群消费模式
         */
        public static final String CLUSTERING = "CLUSTERING";
    }
}
