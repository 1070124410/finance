package com.finance.anubis.core;

import com.finance.anubis.core.model.Task;
import com.finance.anubis.enums.TaskStatus;
import org.springframework.stereotype.Component;

/**
 * 启动所有执行状态的任务
 */
@Component
public class TaskInitializer {


    public void startTask(Task task) {
        if (task.getStatus() == TaskStatus.START) {
            task.listenStart();
        }
    }
}
