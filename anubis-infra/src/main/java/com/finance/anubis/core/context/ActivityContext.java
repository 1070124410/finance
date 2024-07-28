package com.finance.anubis.core.context;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.config.EventResourceConfig;
import com.finance.anubis.config.MessageResourceConfig;
import com.finance.anubis.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;

@Data
@Accessors
@NoArgsConstructor
public class ActivityContext implements Serializable {

    /**
     * 源数据获取执行上下文
     */
    private SourceContext sourceContext;

    /**
     * 目标数据获取执行上下文
     */
    private List<URLSourceContext> targetContexts;

    private Long taskId;

    /**
     * 构建业务唯一属性
     * @param uniqueKeys
     * @param taskName
     * @return
     */
    public String getBizKey(List<String> uniqueKeys,String taskName) {
        Map<String, Object> mappedData = sourceContext.getMappedData();
        List<String> values = new ArrayList<>();
        //前缀为任务名称（英文）
        values.add(taskName );
        for (String uniqueKey : uniqueKeys) {
            values.add(mappedData.get(uniqueKey).toString());
        }
        return String.join("_", values);
    }

    /**
     * 获取源数据用于比对的map
     * @param compareKeys
     * @return
     */
    public Map<String, Object> getCompareSourceData(List<String> compareKeys) {
        Map<String, Object> mappedData = sourceContext.getMappedData();
        Map<String, Object> compareData = new HashMap<>();
        for (String compareKey : compareKeys) {
            compareData.put(compareKey, mappedData.get(compareKey));
        }
        return compareData;
    }

    /**
     * 获取目标数据用于比对的map
     * @param compareKeys
     * @return
     */
    public Map<String, Object> getCompareTargetData(List<String> compareKeys) {
        if (CollectionUtil.isNotEmpty(targetContexts)) {
            Map<String, Object> mappedData = new HashMap<>();
            targetContexts.forEach(targetContext -> mappedData.putAll(targetContext.getMappedData()));
            Map<String, Object> compareData = new HashMap<>();
            compareKeys.forEach(key -> compareData.put(key, mappedData.get(key)));
            return compareData;
        }
       return  Collections.emptyMap();
    }

    /**
     *  目标数据获取完成后，更新上下文
     * @param sourceContext
     */
    public void addTargetURLResourceContext(URLSourceContext sourceContext) {
        if (targetContexts == null) {
            targetContexts = new ArrayList<>();
        }
        targetContexts.add(sourceContext);
    }
    /**
     *  源数据获取完成后，更新上下文
     * @param urlSourceContext
     */
    public void addSourceURLResourceContext(URLSourceContext urlSourceContext) {
        EventSourceContext eventSourceContext = (EventSourceContext) sourceContext;
        eventSourceContext.addURLSourceContext(urlSourceContext);
    }

    /**
     * 重置上下文
     */
    public void remark() {
        clearSourceURLContext();
        clearTargetURLContext();
    }

    /**
     * 清空源数据url执行历史
     */
    public void clearSourceURLContext() {
        if(this.sourceContext instanceof EventSourceContext){
            ((EventSourceContext) this.sourceContext).clearURLContext();
        }
    }

    /**
     * 清空目标数据url执行历史
     */
    public void clearTargetURLContext() {
        if(CollUtil.isNotEmpty(targetContexts)){
            this.targetContexts.clear();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "resourceType",
            visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(
                    value = MessageSourceContext.class,
                    name = "MessageResourceConfig"),
            @JsonSubTypes.Type(
                    value = EventSourceContext.class,
                    name = "EventResourceConfig"),
            @JsonSubTypes.Type(
                    value = URLSourceContext.class,
                    name = "URLResourceConfig")})
    public static abstract class SourceContext implements Serializable {
        protected String sourceKey;

        protected ResourceType resourceType;
        protected Map<String, Object> mappedData;

        public abstract void remark();
    }


    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Data
    public static class URLSourceContext extends SourceContext {
        private String url;
        private String response;


        public URLSourceContext(String url,
                                String response,
                                String sourceKey,
                                Map<String, Object> mappedData
        ) {
            super(sourceKey, ResourceType.URLResourceConfig, mappedData);
            this.url = url;
            this.response = response;
        }


        @Override
        public void remark() {
            mappedData.clear();
            response = null;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class MessageSourceContext extends SourceContext {
        private String group;
        private String topic;
        private List<String> tags;

        private String body;

        public MessageSourceContext(
                String group,
                String topic,
                List<String> tags,
                String body,
                Map<String, Object> mappedData,
                String sourceKey
        ) {
            super(sourceKey, ResourceType.MessageResourceConfig, mappedData);
            this.group = group;
            this.topic = topic;
            this.tags = tags;
            this.body = body;
        }


        @Override
        public void remark() {
        }

        public static MessageSourceContext init(String message, MessageResourceConfig sourceConfig) {
            JSON messageJson = JSONUtil.parse(message);
            Map<String, Object> sourceMapped = new HashMap<>();
            sourceConfig.getDataMapping().forEach((key, value) -> sourceMapped.put(key, JSONUtil.getByPath(messageJson, value)));
            return new ActivityContext.MessageSourceContext(
                    sourceConfig.getGroup(),
                    sourceConfig.getTopic(),
                    sourceConfig.getTags(),
                    message,
                    sourceMapped,
                    sourceConfig.getKey());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class EventSourceContext extends SourceContext {

        private MessageSourceContext messageSourceContext;
        private List<URLSourceContext> urlSourceContexts = new ArrayList<>();

        public EventSourceContext(String sourceKey,
                                  MessageSourceContext messageSourceContext) {
            super(sourceKey, ResourceType.EventResourceConfig, messageSourceContext.getMappedData());
            this.messageSourceContext = messageSourceContext;
        }

        public void clearURLContext() {
            urlSourceContexts.clear();
        }

        public void addURLSourceContext(URLSourceContext urlSourceContext) {
            urlSourceContexts.add(urlSourceContext);
            super.getMappedData().putAll(urlSourceContext.getMappedData());
        }

        public static EventSourceContext init(String message, EventResourceConfig eventResourceConfig) {
            MessageSourceContext messageSourceContext = MessageSourceContext.init(message, eventResourceConfig);
            return new ActivityContext.EventSourceContext(eventResourceConfig.getKey(), messageSourceContext);
        }

        @Override
        public void remark() {
            urlSourceContexts.clear();
        }
    }
}
