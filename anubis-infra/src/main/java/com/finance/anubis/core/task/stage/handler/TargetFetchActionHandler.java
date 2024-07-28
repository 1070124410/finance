package com.finance.anubis.core.task.stage.handler;

import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.config.ResourceConfig;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.config.URLResourceConfig;
import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.context.ActivityContext;
import com.finance.anubis.core.task.model.TaskActivity;
import com.guming.mq.api.MessageProducer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 目标数据获取执行器
 */
@Component
public class TargetFetchActionHandler extends ActionHandler {
    public TargetFetchActionHandler(TaskActivityRepository taskActivityRepository,
                                    RestTemplate restTemplate,
                                    MessageProducer messageProducer) {
        super(Action.TARGET_FETCH, taskActivityRepository,restTemplate, messageProducer);
    }

    @Override
    protected void innerHandle(TaskActivity taskActivity) {
        ActivityContext activityContext = taskActivity.getContext();
        OnLineTaskConfig onLineTaskConfig = taskActivity.getOnLineTaskConfig();
        activityContext.clearTargetURLContext();
        for (ResourceConfig urlResourceConfig : onLineTaskConfig.getTargetConfigs()) {
            ActivityContext.URLSourceContext urlSourceContext = executeUrlSource((URLResourceConfig) urlResourceConfig, taskActivity);
            if (urlSourceContext != null) {
                activityContext.addTargetURLResourceContext(urlSourceContext);
            }
        }
    }
}
