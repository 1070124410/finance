package com.finance.anubis.service.impl;

import com.finance.anubis.config.OffLineTaskConfig;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.core.model.Task;
import com.finance.anubis.dto.OffLineDataReadyDTO;
import com.finance.anubis.enums.OffLineAction;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.redis.RedisDistributedLock;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.TaskRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.finance.anubis.service.OffLineTaskService;
import com.finance.anubis.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 20:00
 * @Description
 **/
@Service
public class OffLineTaskServiceImpl implements OffLineTaskService {
    public final static Logger log = LoggerFactory.getLogger(OffLineTaskServiceImpl.class);
    private final TaskRepository taskRepository;

    private final MessageProducer messageProducer;

    private final OffLineTaskActivityRepository taskActivityRepository;

    private final RedisDistributedLock distributedLockProvider;


    public OffLineTaskServiceImpl(TaskRepository taskRepository, MessageProducer messageProducer, OffLineTaskActivityRepository taskActivityRepository, RedisDistributedLock distributedLockProvider) {
        this.taskRepository = taskRepository;
        this.messageProducer = messageProducer;
        this.taskActivityRepository = taskActivityRepository;
        this.distributedLockProvider = distributedLockProvider;
    }


    @Override
    public Boolean dataReady(OffLineDataReadyDTO dto) {
        log.info("receive dataReady,taskName:{},configKey:{},verifyDate:{}", dto.getTaskName(), dto.getConfigKey(), dto.getVerifyDate());
        //校验是否有此对账任务和key
        Task task = taskRepository.selectByName(dto.getTaskName());
        OffLineTaskConfig config = (OffLineTaskConfig) task.getTaskConfig();
        String sourceKey = config.getSourceConfig().getKey();
        String targetKey = config.getTargetConfig().getKey();
        if ((task == null) || (!sourceKey.equals(dto.getConfigKey()) && !targetKey.equals(dto.getConfigKey()))) {
            return false;
        }
        String bizKey = OffLineTaskActivity.getBizKey(dto.getTaskName(), dto.getVerifyDate());
        OffLineTaskActivity taskActivity = taskActivityRepository.getByBizKey(bizKey);
        if (taskActivity == null) {
            OffLineTaskActivity activity = OffLineTaskActivity.init();
            activity.setTaskConfig((OffLineTaskConfig) task.getTaskConfig());
            activity.initSourceContext();
            activity.getContext().setVerifyDate(StringUtils.isBlank(dto.getVerifyDate()) ? LocalDateTime.now() : LocalDateTime.parse(dto.getVerifyDate()));
            taskActivityRepository.save(activity);
            taskActivity = activity;
        }
        taskActivity.initRequestParam(dto.getConfigKey(), dto.getRequestParam(), dto.getCustom());
        taskActivity.setVerifyDate(dto.getVerifyDate());
        try {
            OffLineTaskActivity finalTaskActivity = taskActivity;
            distributedLockProvider.tryApplyWithinLockScopeInterruptibly(1, TimeUnit.SECONDS, () -> {
                taskActivityRepository.toDataFetch(finalTaskActivity.getId(), finalTaskActivity.getContext(), finalTaskActivity.getAction());

            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while trying to apply lock.", e);
        }
        OffLineActionMqBody offLineActionMqBody = new OffLineActionMqBody(bizKey, dto.getConfigKey());

        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, OffLineAction.DATA_FETCH.getCode(), JsonUtil.toJson(offLineActionMqBody));
        return true;
    }
}

