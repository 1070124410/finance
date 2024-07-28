package com.finance.anubis.core.task.model;

import cn.hutool.core.collection.CollUtil;
import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.constants.enums.ActionResult;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.context.ActivityContext;
import lombok.CustomLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finance.anubis.core.constants.Constants.ACTIVITY_INIT_EXEC_TIMES;

@EqualsAndHashCode(callSuper = true)
@Data
@CustomLog
public class TaskActivity extends BaseModel {
    private ActivityContext context;
    private OnLineTaskConfig onLineTaskConfig;
    private Action action;
    private Integer times;


    public static TaskActivity init() {
        TaskActivity taskActivity = new TaskActivity();
        taskActivity.setAction(Action.INIT);
        taskActivity.setTimes(ACTIVITY_INIT_EXEC_TIMES);
        LocalDateTime now = LocalDateTime.now();
        taskActivity.setCreateTime(now);
        taskActivity.setUpdateTime(now);
        return taskActivity;
    }

    public TaskActivityResult getResult() {
        if (this.action != Action.DONE) {
            return buildUnreadyTaskActivityResult();
        }
        Map<String, Object> compareSourceData = getContext().getCompareSourceData(onLineTaskConfig.getCompareKeys());
        Map<String, Object> compareTargetData = getContext().getCompareTargetData(onLineTaskConfig.getCompareKeys());
        TaskActivityResult taskActivityResult = new TaskActivityResult();
        taskActivityResult.setTaskActivityId(this.id);
        taskActivityResult.setCompareSourceData(compareSourceData);
        taskActivityResult.setCompareTargetData(compareTargetData);
        List<String> varianceKeys = new ArrayList<>();
        compareSourceData.forEach((key, sourceValue) -> {
            Object targetValue = compareTargetData.get(key);
            //特殊处理：目前都为空当做比对通过
            if (targetValue == null && sourceValue == null) {
                log.error("比对源数据和目标数据均为空,taskActivityId:{},key:{}", this.id, key);
            } else if (targetValue == null || sourceValue == null) {
                varianceKeys.add(key);
            } else if (targetValue.getClass().equals(sourceValue.getClass())) {
                if (!sourceValue.equals(targetValue)) {
                    varianceKeys.add(key);
                }
            } else {
                if (!sourceValue.toString().equals(targetValue.toString())) {
                    varianceKeys.add(key);
                }
            }
        });
        if (CollUtil.isEmpty(varianceKeys)) {
            taskActivityResult.setActionResult(ActionResult.ACCORD);
        } else {
            taskActivityResult.setActionResult(ActionResult.VARIANCE);
        }
        taskActivityResult.setVarianceKeys(varianceKeys);
        taskActivityResult.setBizKey(this.getBizKey());
        taskActivityResult.setCompareKeys(onLineTaskConfig.getCompareKeys());
        taskActivityResult.setTimes(this.times);
        return taskActivityResult;
    }

    private TaskActivityResult buildUnreadyTaskActivityResult() {
        TaskActivityResult taskActivityResult = new TaskActivityResult();
        taskActivityResult.setTaskActivityId(this.id);
        taskActivityResult.setActionResult(ActionResult.UNREADY);
        return taskActivityResult;
    }


    public Map<String, Map<String, Object>> getContextMappedData() {
        Map<String, Map<String, Object>> mappedData = new HashMap<>();
        ActivityContext.SourceContext sourceContext = context.getSourceContext();
        buildRequestMappedData(mappedData, sourceContext);
        List<ActivityContext.URLSourceContext> targetContexts = context.getTargetContexts();
        if (CollUtil.isNotEmpty(targetContexts)) {
            targetContexts.forEach(targetContext -> {
                buildRequestMappedData(mappedData, targetContext);
            });
        }

        return mappedData;
    }

    private <T extends ActivityContext.SourceContext> void buildRequestMappedData(Map<String, Map<String, Object>> mappedData, T sourceContext) {
        ResourceType resourceType = sourceContext.getResourceType();
        if (resourceType == ResourceType.URLResourceConfig) {
            mappedData.putAll(getContextMappedData((ActivityContext.URLSourceContext) sourceContext));
        } else if (resourceType == ResourceType.MessageResourceConfig) {
            mappedData.putAll(getContextMappedData((ActivityContext.MessageSourceContext) sourceContext));
        }
        if (resourceType == ResourceType.EventResourceConfig) {
            mappedData.putAll(getContextMappedData((ActivityContext.EventSourceContext) sourceContext));
        }
    }

    private Map<String, Map<String, Object>> getContextMappedData(ActivityContext.URLSourceContext urlSourceContext) {
        HashMap<String, Map<String, Object>> urlMappedData = new HashMap<>();
        urlMappedData.put(urlSourceContext.getSourceKey(), urlSourceContext.getMappedData());
        return urlMappedData;
    }

    private Map<String, Map<String, Object>> getContextMappedData(ActivityContext.MessageSourceContext messageSourceContext) {
        HashMap<String, Map<String, Object>> urlMappedData = new HashMap<>();
        urlMappedData.put(messageSourceContext.getSourceKey(), messageSourceContext.getMappedData());
        return urlMappedData;
    }

    private Map<String, Map<String, Object>> getContextMappedData(ActivityContext.EventSourceContext eventSourceContext) {
        ActivityContext.MessageSourceContext messageSourceContext = eventSourceContext.getMessageSourceContext();
        Map<String, Map<String, Object>> messageMappedParams = getContextMappedData(messageSourceContext);
        Map<String, Map<String, Object>> eventMappedParams = new HashMap<>(messageMappedParams);
        for (ActivityContext.URLSourceContext urlSourceContext : eventSourceContext.getUrlSourceContexts()) {
            Map<String, Map<String, Object>> urlMappedParams = getContextMappedData(urlSourceContext);
            messageMappedParams.putAll(urlMappedParams);
        }
        return eventMappedParams;
    }


    public void remark() {
        this.times = this.times + 1;
        this.context.remark();
        this.action = Action.REMARK;
    }


    public String getBizKey() {
        return context.getBizKey(onLineTaskConfig.getUniqueKeys(), onLineTaskConfig.getName());
    }
}
