package com.finance.anubis.core.task.stage.handler;

import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.enums.Action;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.TaskActivityRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 重置action执行器
 */
@Component
public class RemarkActionHandler extends ActionHandler {


    public RemarkActionHandler(TaskActivityRepository taskActivityRepository,
                               RestTemplate restTemplate,
                               MessageProducer messageProducer) {
        super(Action.REMARK, taskActivityRepository, restTemplate,messageProducer);
    }

    @Override
    public void innerHandle(TaskActivity taskActivity) {
        //taskActivity.remark();
    }
}
