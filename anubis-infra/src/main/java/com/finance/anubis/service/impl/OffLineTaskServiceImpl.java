package com.finance.anubis.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.TaskRepository;
import com.guming.fd.distributed.lock.DistributedLock;
import com.guming.fd.distributed.lock.DistributedLockProvider;
import com.finance.anubis.core.config.OffLineTaskConfig;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.task.model.Task;
import com.finance.anubis.dto.OffLineDataReadyDTO;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.finance.anubis.service.OffLineTaskService;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 20:00
 * @Description
 **/
@Service
@CustomLog
public class OffLineTaskServiceImpl implements OffLineTaskService {

    private final TaskRepository taskRepository;

    private final MessageProducer messageProducer;

    private final OffLineTaskActivityRepository taskActivityRepository;

    private final DistributedLockProvider distributedLockProvider;


    public OffLineTaskServiceImpl(TaskRepository taskRepository, MessageProducer messageProducer, OffLineTaskActivityRepository taskActivityRepository, DistributedLockProvider distributedLockProvider) {
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
        DistributedLock distributedLock = distributedLockProvider.getOrCreate(taskActivity.getBizKey());
        try {
            //分布式锁保证并发情况下只更新context中自己对应的部分
            OffLineTaskActivity finalTaskActivity = taskActivity;
            distributedLock.tryApplyWithinLockScopeInterruptibly(1, TimeUnit.SECONDS, () -> {
                taskActivityRepository.toDataFetch(finalTaskActivity.getId(), finalTaskActivity.getContext(), finalTaskActivity.getAction());
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        OffLineActionMqBody offLineActionMqBody = new OffLineActionMqBody(bizKey, dto.getConfigKey());

        Message message = MessageBuilder.create()
                .topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                .tag(OffLineAction.DATA_FETCH.getCode())
                .body(JSONUtil.parse(offLineActionMqBody))
                .build();
        messageProducer.syncSend(message);
        return true;
    }
}

