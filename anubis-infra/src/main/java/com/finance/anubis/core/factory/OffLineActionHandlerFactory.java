package com.finance.anubis.core.factory;

import com.finance.anubis.core.task.stage.offlineHandler.OffLineActionHandler;
import com.finance.anubis.enums.OffLineAction;
import lombok.CustomLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OffLineActionHandlerFactory extends StageHandlerFactory {
    public final static Logger log = LoggerFactory.getLogger(OffLineActionHandlerFactory.class);
    private static final Map<OffLineAction, OffLineActionHandler> OFFLINE_STAGE_HANDLER_MAP = new EnumMap<>(OffLineAction.class);


    public OffLineActionHandler getOffLineHandler(OffLineAction action) {
        return OFFLINE_STAGE_HANDLER_MAP.get(action);
    }


    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(OffLineActionHandler.class)
                .values()
                .forEach(handler -> {
                    if (OFFLINE_STAGE_HANDLER_MAP.containsKey(handler.getAction())) {
                        OffLineActionHandler actionHandler = OFFLINE_STAGE_HANDLER_MAP.get(handler.getAction());
                        log.error("duplicate action handler {},{}", actionHandler.getClass().getName(), handler.getClass().getName());
                        throw new RuntimeException("duplicate action handler");
                    }
                    OFFLINE_STAGE_HANDLER_MAP.put(handler.getAction(), handler);
                });
    }
}
