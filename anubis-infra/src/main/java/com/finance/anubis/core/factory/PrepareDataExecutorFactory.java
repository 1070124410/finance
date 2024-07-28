package com.finance.anubis.core.factory;

import com.finance.anubis.core.constants.enums.OffLineResourceType;
import com.finance.anubis.core.task.executor.PrepareDataTemplate;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@CustomLog
public class PrepareDataExecutorFactory extends StageHandlerFactory {

    private static final Map<OffLineResourceType, PrepareDataTemplate> PREPARE_DATA_EXECUTOR_MAP = new EnumMap<>(OffLineResourceType.class);

    public PrepareDataTemplate getPrepareDataExecutor(OffLineResourceType type) {
        return PREPARE_DATA_EXECUTOR_MAP.get(type);
    }

    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(PrepareDataTemplate.class)
                .values()
                .forEach(template -> {
                    if (PREPARE_DATA_EXECUTOR_MAP.containsKey(template.getType())) {
                        PrepareDataTemplate prepareDataTemplate = PREPARE_DATA_EXECUTOR_MAP.get(template.getType());
                        log.error("duplicate prepareDataTemplate {},{}", prepareDataTemplate.getClass().getName(), template.getClass().getName());
                        throw new RuntimeException("duplicate prepareDataTemplate");
                    }
                    PREPARE_DATA_EXECUTOR_MAP.put(template.getType(), template);
                });
    }
}
