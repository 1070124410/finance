package com.finance.anubis.core.task.stage.handler;

import cn.hutool.json.JSONUtil;
import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.enums.Action;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.ActivityResultRepository;
import com.finance.anubis.repository.TaskActivityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;

/**
 * 比较action执行器
 */
@Component
public class ActivityCompareHandler extends ActionHandler {

    private final String splitCharacter = "_";

    @Resource
    private ActivityResultRepository activityResultRepository;

    public ActivityCompareHandler(TaskActivityRepository taskActivityRepository,
                                  RestTemplate restTemplate,
                                  MessageProducer messageProducer) {
        super(Action.COMPARE, taskActivityRepository, restTemplate, messageProducer);
    }

    @Override
    public void innerHandle(TaskActivity taskActivity) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterHandle(TaskActivity taskActivity) {
        activityResultRepository.save(taskActivity.getResult());
        //发送mq
        String tag = new StringBuilder()
                .append(taskActivity.getOnLineTaskConfig().getName())
                .append(splitCharacter)
                .append(taskActivity.getAction().name())
                .toString();
        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, tag, JSONUtil.toJsonStr(taskActivity.getResult()));
    }
}
