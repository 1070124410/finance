package com.finance.anubis.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.adapter.TaskAdapter;
import com.finance.anubis.config.OnLineTaskConfig;
import com.finance.anubis.core.model.Task;
import com.finance.anubis.core.task.runner.MQSourceRunner;
import com.finance.anubis.enums.TaskStatus;
import com.finance.anubis.exception.Status;
import com.finance.anubis.exception.StatusCodeEnum;
import com.finance.anubis.exception.StatusCodeException;
import com.finance.anubis.repository.TaskRepository;
import com.finance.anubis.request.TaskReq;
import com.finance.anubis.response.TaskRes;
import com.finance.anubis.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 14:07
 * @Description
 **/
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskRes selectById(Long id) {
        if (null == id) {
            return null;
        }
        try {
            Task task = taskRepository.selectById(id);
            return TaskAdapter.adapt2TaskRes(task);
        } catch (Exception e) {
            throw new StatusCodeException(Status.error(StatusCodeEnum.TaskNotComplete.getMessage()));
        }
    }

    @Override
    public List<TaskRes> selectTaskByParam(TaskReq taskReq) {
        if (null == taskReq) {
            return Collections.emptyList();
        }
        try {
            List<Task> tasks = taskRepository.selectTaskByParam(TaskAdapter.adapt2TaskEntity(taskReq));
            return tasks.stream().map(TaskAdapter::adapt2TaskRes).collect(Collectors.toList());
        } catch (StatusCodeException e) {
            throw new StatusCodeException(Status.error("Task异常"));
        }
    }


    @Override
    public TaskRes add(TaskReq taskReq) {
        if (null == taskReq) {
            throw new StatusCodeException(Status.error(StatusCodeEnum.NullParam.getMessage()));
        }
        try {
            Task task = TaskAdapter.adapt2Task(taskReq);
            Task res = taskRepository.add(task);
            return TaskAdapter.adapt2TaskRes(res);
        } catch (StatusCodeException e) {
            throw new StatusCodeException(Status.error(e.getStatusCode().getReason()));
        }
    }

    @Override
    public TaskRes updateTask(TaskReq taskReq) {
        if (null == taskReq) {
            throw new StatusCodeException(Status.error(StatusCodeEnum.NullParam.getMessage()));
        }
        try {
            Task task = TaskAdapter.adapt2Task(taskReq);
            Task res = taskRepository.updateTask(task);
            return TaskAdapter.adapt2TaskRes(res);
        } catch (StatusCodeException e) {
            throw new StatusCodeException(Status.error(e.getStatusCode().getReason()));
        }
    }


    @Override
    public boolean start(Long taskId) {
        if (taskId == null || taskId <= 0) {
            return false;
        }
        return taskRepository.start(taskId);
    }


    @Override
    public boolean stop(Long taskId) {
        if (taskId == null || taskId <= 0) {
            return false;
        }
        return taskRepository.stop(taskId);
    }


    @Override
    public String compensateMessage(String messageBodyStr, Long taskId) {
        if (StringUtils.isBlank(messageBodyStr) || taskId == null || taskId <= 0) {
            throw new StatusCodeException(Status.error("参数异常"));
        }
        Task task = taskRepository.selectById(taskId);
        if (task == null || task.getStatus() != TaskStatus.START) {
            throw new StatusCodeException(Status.error("任务状态异常"));
        }
        String sourceConsumerBeanName = task.getTaskConfig().getName() + "SourceConsumer";
        MQSourceRunner.MQConsumer consumer = (MQSourceRunner.MQConsumer) SpringUtil.getApplicationContext().getBean(sourceConsumerBeanName);
        if (null == consumer) {
            throw new StatusCodeException(Status.error("任务状态异常"));
        }
        Message message = new Message();
        OnLineTaskConfig config = (OnLineTaskConfig) task.getTaskConfig();
        message.setTopic(config.getSourceConfig().getTopic());
        message.setTag(config.getSourceConfig().getTags().toString());
        message.setKey(config.getSourceConfig().getKey());
        message.setBody(messageBodyStr.getBytes());

        return consumer.consume(message, new ConsumeContext()).toString();
    }

}
