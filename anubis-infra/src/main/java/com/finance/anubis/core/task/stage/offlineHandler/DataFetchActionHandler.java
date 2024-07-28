package com.finance.anubis.core.task.stage.offlineHandler;

import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.guming.fd.distributed.lock.DistributedLock;
import com.guming.fd.distributed.lock.DistributedLockProvider;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.constants.enums.OffLineResourceType;
import com.finance.anubis.core.constants.enums.SourceDataStatus;
import com.finance.anubis.core.constants.enums.StatusResult;
import com.finance.anubis.core.factory.PrepareDataExecutorFactory;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL;
import static com.finance.anubis.core.constants.enums.SourceDataStatus.DATA_READY;

/**
 * action 执行器
 */
@CustomLog
@Component
public class DataFetchActionHandler extends OffLineActionHandler {


    private final OffLineTaskActivityRepository activityRepository;

    private final MessageProducer messageProducer;

    private final PrepareDataExecutorFactory dataExecutorFactory;

    private final DistributedLockProvider distributedLockProvider;

    public DataFetchActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                  PrepareDataExecutorFactory dataExecutorFactory, DistributedLockProvider distributedLockProvider) {
        super(OffLineAction.DATA_FETCH);
        this.activityRepository = taskActivityRepository;
        this.messageProducer = messageProducer;
        this.dataExecutorFactory = dataExecutorFactory;
        this.distributedLockProvider = distributedLockProvider;
    }


    @Override
    protected StatusResult checkStatus(OffLineTaskActivity taskActivity, String key) {
        //检查对账文件是否已就绪
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();
        SourceDataStatus sourceDataStatus = taskActivity.getSourceContext(sourceKey).getSourceDataStatus();
        SourceDataStatus targetDataStatus = taskActivity.getSourceContext(targetKey).getSourceDataStatus();
        SourceDataStatus dataStatus = taskActivity.getSourceContext(key).getSourceDataStatus();
        if (!DATA_READY.equals(dataStatus)) {
            return StatusResult.ACT;
        } else if (DATA_READY.equals(sourceDataStatus) && DATA_READY.equals(targetDataStatus)) {
            return StatusResult.ONLY_PUSH;
        } else {
            return StatusResult.IGNORE;
        }
    }

    @Override
    protected void innerHandle(OffLineTaskActivity taskActivity, String key) {
        //创建对账文件保存文件夹(先校验存在,不会重复创建)
        File file = new File(taskActivity.getAbsoluteFilePath().toString());
        file.mkdirs();
        //根据对账文件提供方式获取对账数据
        OffLineResourceType resourceType = OffLineResourceType.of(taskActivity.getResouceConfig(key).getResourceType());
        dataExecutorFactory.getPrepareDataExecutor(resourceType).handle(taskActivity, key);
        taskActivity.getSourceContext(key).setSourceDataStatus(DATA_READY);
    }

    @Override
    protected void updateActivity(OffLineTaskActivity taskActivity, String key) {
        DistributedLock distributedLock = distributedLockProvider.getOrCreate(taskActivity.getBizKey());
        try {
            //分布式锁保证并发情况下只更新context中自己对应的部分
            distributedLock.tryApplyWithinLockScopeInterruptibly(1, TimeUnit.SECONDS, () -> {
                OffLineTaskActivity activity = activityRepository.getByBizKey(taskActivity.getBizKey());
                activity.getContext().getSourceContext().put(key, taskActivity.getSourceContext(key));
                activityRepository.updateContext(activity.getId(), activity.getContext());
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //判断双方对账数据是否全部就绪(并发情况下因分布式锁影响只有后者会更新Action)
        OffLineTaskActivity activity = activityRepository.getByBizKey(taskActivity.getBizKey());
        String sourceKey = activity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = activity.getTaskConfig().getTargetConfig().getKey();
        SourceDataStatus sourceDataStatus = activity.getSourceContext(sourceKey).getSourceDataStatus();
        SourceDataStatus targetDataStatus = activity.getSourceContext(targetKey).getSourceDataStatus();
        if ((DATA_READY.equals(sourceDataStatus) && DATA_READY.equals(targetDataStatus))) {
            activityRepository.toDataCompareTotal(activity.getId(), activity.getAction());
            taskActivity.setAction(DATA_COMPARE_TOTAL);
        }
    }

    @Override
    protected void afterHandle(OffLineTaskActivity taskActivity, String key) {
        if (taskActivity.getAction().equals(DATA_COMPARE_TOTAL)) {
            OffLineActionMqBody dto = new OffLineActionMqBody(taskActivity.getBizKey(), key);
            Message message = MessageBuilder.create()
                    .topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                    .tag(DATA_COMPARE_TOTAL.getCode())
                    .body(dto)
                    .build();
            messageProducer.syncSend(message);
        }
    }

}
