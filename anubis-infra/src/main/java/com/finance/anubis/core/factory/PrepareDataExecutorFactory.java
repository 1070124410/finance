package com.finance.anubis.core.factory;

import com.finance.anubis.core.task.executor.PrepareDataTemplate;
import com.finance.anubis.enums.OffLineResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class PrepareDataExecutorFactory extends StageHandlerFactory {
    public final static Logger log = LoggerFactory.getLogger(PrepareDataExecutorFactory.class);
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
