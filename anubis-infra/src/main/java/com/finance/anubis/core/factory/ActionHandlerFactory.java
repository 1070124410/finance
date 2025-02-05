package com.finance.anubis.core.factory;

import com.finance.anubis.core.task.stage.handler.ActionHandler;
import com.finance.anubis.enums.Action;
import lombok.CustomLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ActionHandlerFactory extends StageHandlerFactory {
    public final static Logger log = LoggerFactory.getLogger(ActionHandlerFactory.class);
    private static final Map<Action, ActionHandler> STAGE_HANDLER_MAP = new EnumMap<>(Action.class);

    public ActionHandler getHandler(Action action) {
        return STAGE_HANDLER_MAP.get(action);
    }


    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(ActionHandler.class)
                .values()
                .forEach(handler -> {
                    if (STAGE_HANDLER_MAP.containsKey(handler.getAction())) {
                        ActionHandler actionHandler = STAGE_HANDLER_MAP.get(handler.getAction());
                        log.error("duplicate action handler {},{}", actionHandler.getClass().getName(), handler.getClass().getName());
                        throw new RuntimeException("duplicate action handler");
                    }
                    STAGE_HANDLER_MAP.put(handler.getAction(), handler);
                });
    }
}
