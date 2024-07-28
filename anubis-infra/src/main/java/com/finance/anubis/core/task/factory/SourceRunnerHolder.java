package com.finance.anubis.core.task.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.finance.anubis.core.task.runner.MQSourceRunner;
import com.finance.anubis.core.task.runner.SourceRunner;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.config.EventResourceConfig;
import com.finance.anubis.core.config.MessageResourceConfig;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.config.TaskConfig;
import com.finance.anubis.core.constants.enums.MessageInfraType;
import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.task.executor.SourceExecutor;
import com.guming.mq.rocket.DecorateUtils;

public class SourceRunnerHolder {
    public static SourceRunner getRunner(TaskConfig taskConfig, Long taskId) {
        OnLineTaskConfig onLineTaskConfig = (OnLineTaskConfig) taskConfig;
        String sourceRunnerBeanName = onLineTaskConfig.getName() + "SourceRunner";
        boolean containRunner = SpringUtil.getApplicationContext().containsBean(sourceRunnerBeanName);
        if (containRunner) {
            return SpringUtil.getBean(sourceRunnerBeanName, SourceRunner.class);
        }
        MessageResourceConfig sourceConfig = onLineTaskConfig.getSourceConfig();
        MessageInfraType messageInfraType = sourceConfig.getMessageInfraType();
        SourceExecutor sourceExecutor = SpringUtil.getBean(SourceExecutor.class);

        if (messageInfraType == null) {
            throw new RuntimeException("");
        }
        SourceRunner sourceRunner = null;
        switch (messageInfraType) {
            case Kafka:
                break;
            case Ons:
            case RocketMq:
                MQSourceRunner.MQConsumer mqConsumer = getConsumer(onLineTaskConfig, taskId, sourceExecutor);
                if (onLineTaskConfig.getSourceConfig().getResourceType() == ResourceType.MessageResourceConfig) {
                    sourceRunner = new MQSourceRunner(mqConsumer, onLineTaskConfig.getSourceConfig());
                } else if (onLineTaskConfig.getSourceConfig().getResourceType() == ResourceType.EventResourceConfig) {
                    sourceRunner = new MQSourceRunner(mqConsumer, (EventResourceConfig) onLineTaskConfig.getSourceConfig());
                }
                SpringUtil.registerBean(sourceRunnerBeanName, sourceRunner);
                break;
            default:
                break;
        }
        return sourceRunner;
    }

    private static MQSourceRunner.MQConsumer getConsumer(TaskConfig taskConfig, Long taskId, SourceExecutor sourceExecutor) {
        OnLineTaskConfig onLineTaskConfig = (OnLineTaskConfig) taskConfig;
        String sourceConsumerBeanName = onLineTaskConfig.getName() + "SourceConsumer";
        boolean containConsumerBean = SpringUtil.getApplicationContext().containsBean(sourceConsumerBeanName);
        MQSourceRunner.MQConsumer mqConsumer;
        if (containConsumerBean) {
            mqConsumer = SpringUtil.getBean(sourceConsumerBeanName, MQSourceRunner.MQConsumer.class);
        } else {
            mqConsumer = new MQSourceRunner.MQConsumer(
                    SpringUtil.getBean(TaskActivityRepository.class),
                    sourceExecutor, onLineTaskConfig,
                    DecorateUtils.decorateGroup(onLineTaskConfig.getSourceConfig().getGroup()), taskId);
            SpringUtil.registerBean(sourceConsumerBeanName, mqConsumer);
        }
        return mqConsumer;
    }
}
