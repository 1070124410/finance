package com.finance.anubis.core.task.model;

import com.finance.anubis.core.task.runner.SourceRunner;
import com.finance.anubis.core.config.TaskConfig;
import com.finance.anubis.core.constants.enums.TaskStatus;
import com.finance.anubis.core.constants.enums.TaskType;
import com.finance.anubis.core.task.factory.SourceRunnerHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Task extends BaseModel {
    private TaskStatus status;

    private TaskType taskType;

    private TaskConfig taskConfig;

    public void listenStart() {
        SourceRunner sourceRunner = SourceRunnerHolder.getRunner(taskConfig, this.id);
        sourceRunner.start();
    }

    public void listenStop() {
        SourceRunner sourceRunner = SourceRunnerHolder.getRunner(taskConfig, this.id);
        sourceRunner.stop();
    }

    public TaskConfig start() {
        if (getStatus() == TaskStatus.START) {
            throw new RuntimeException("任务状态错误");
        }
        setStatus(TaskStatus.START);
        listenStart();
        return taskConfig;
    }

    public TaskConfig stop() {
        if (getStatus() == TaskStatus.STOP) {
            throw new RuntimeException("任务状态错误");
        }
        setStatus(TaskStatus.STOP);
        listenStop();
        return taskConfig;
    }

}
