package com.finance.anubis.core.task.stage.handler;

import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.task.model.TaskActivity;
import com.guming.mq.api.MessageProducer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 初始化action 执行器
 */
@Component
public class InitActionHandler extends ActionHandler {


    public InitActionHandler(TaskActivityRepository taskActivityRepository,
                             RestTemplate restTemplate,
                             MessageProducer messageProducer) {
        super(Action.INIT, taskActivityRepository,restTemplate, messageProducer);
    }

    @Override
    public void innerHandle(TaskActivity taskActivity) {
        return;
    }
}
