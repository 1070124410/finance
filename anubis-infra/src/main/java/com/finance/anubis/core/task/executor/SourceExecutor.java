package com.finance.anubis.core.task.executor;

import com.finance.anubis.core.factory.ActionHandlerFactory;
import com.finance.anubis.core.task.model.TaskActivity;
import org.springframework.stereotype.Component;

@Component
public class SourceExecutor {
    ActionHandlerFactory actionHandlerFactory;

    SourceExecutor(ActionHandlerFactory actionHandlerFactory) {
        this.actionHandlerFactory = actionHandlerFactory;
    }

    public void handle(TaskActivity taskActivity) {
        actionHandlerFactory.getHandler(taskActivity.getAction()).handle(taskActivity);
    }

}
