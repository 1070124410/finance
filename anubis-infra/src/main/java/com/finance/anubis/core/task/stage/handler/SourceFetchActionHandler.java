package com.finance.anubis.core.task.stage.handler;

import cn.hutool.json.JSONUtil;
import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.config.EventResourceConfig;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.config.URLResourceConfig;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.context.ActivityContext;
import com.finance.anubis.core.task.model.TaskActivity;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 原数据获取执行器
 */
@Component
@CustomLog
public class SourceFetchActionHandler extends ActionHandler {
    public SourceFetchActionHandler(TaskActivityRepository taskActivityRepository,
                                    RestTemplate restTemplate,
                                    MessageProducer messageProducer) {
        super(Action.SOURCE_FETCH, taskActivityRepository, restTemplate, messageProducer);
    }

    @Override
    public void innerHandle(TaskActivity taskActivity) {
        ActivityContext activityContext = taskActivity.getContext();
        OnLineTaskConfig onLineTaskConfig = taskActivity.getOnLineTaskConfig();
        ResourceType resourceType = onLineTaskConfig.getSourceConfig().getResourceType();
        activityContext.clearSourceURLContext();
        if (resourceType == ResourceType.EventResourceConfig) {
            EventResourceConfig eventResourceConfig = (EventResourceConfig) onLineTaskConfig.getSourceConfig();
            List<ActivityContext.URLSourceContext> urlSourceContextList = new ArrayList<>();
            boolean conflict = false;
            for (URLResourceConfig urlResourceConfig : eventResourceConfig.getUrlResourceConfigs()) {
                if (conflict) {
                    break;
                }
                ActivityContext.URLSourceContext urlSourceContext = executeUrlSource(urlResourceConfig, taskActivity);
                if (urlSourceContext != null) {
                    //查看是否冲突源uniqueKey数据
                    Map<String, Object> mappedData = activityContext.getSourceContext().getMappedData();
                    for (String uniqueKey : onLineTaskConfig.getUniqueKeys()) {
                        if (urlSourceContext.getMappedData().containsKey(uniqueKey)) {
                            String source = String.valueOf(mappedData.get(uniqueKey));
                            String target = String.valueOf(urlSourceContext.getMappedData().get(uniqueKey));
                            if (!source.equals(target)) {
                                log.error("源数据冲突,key:{},源数据:{},新数据:{}", uniqueKey, source, target);
                                conflict = true;
                                break;
                            }
                        }
                    }
                    urlSourceContextList.add(urlSourceContext);
                }
            }
            if (!urlSourceContextList.isEmpty()) {
                for (ActivityContext.URLSourceContext context : urlSourceContextList) {
                    if (conflict) {
                        context.getMappedData().clear();
                    }
                    activityContext.addSourceURLResourceContext(context);
                }
            }

        }

    }

    @Override
    public void afterHandle(TaskActivity taskActivity) {
        // 推送到mq
        int delay = taskActivity.getOnLineTaskConfig().getDelay();
        if (delay > 0) {
            Message message = MessageBuilder.create().topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                    .tag(taskActivity.getAction().name())
                    .body(JSONUtil.parse(taskActivity.getResult()))
                    .build();
            messageProducer.sendDelayMsg(message, delay, TimeUnit.SECONDS);
        } else {
            super.afterHandle(taskActivity);
        }
    }
}
