package com.finance.anubis.core.job;

import cn.hutool.json.JSONUtil;
import com.finance.anubis.core.constants.enums.Cmd;
import com.finance.anubis.core.task.model.Task;
import com.finance.anubis.repository.TaskRepository;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.util.ShardingUtil;
import lombok.CustomLog;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@JobHandler("taskActionJobHandler")
@CustomLog
public class TaskActionJobHandler extends IJobHandler {

    @Resource
    private TaskRepository taskRepository;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        ShardingUtil.ShardingVO shardingVo = ShardingUtil.getShardingVo();
        log.info("task action job handle sharing info:{}",JSONUtil.toJsonStr(shardingVo));
        try {
            TaskCmd taskCmd = JSONUtil.toBean(param, TaskCmd.class);
            Task task = taskRepository.selectById(taskCmd.taskId);
            if (taskCmd.getCmd().equals(Cmd.STOP)) {
                task.listenStop();
            } else if (taskCmd.getCmd().equals(Cmd.START)) {
                task.listenStart();
            }
            return ReturnT.SUCCESS;
        }catch (Exception e){
            log.error("task action job handle failed. param {}",param,e);
            throw e;
        }
    }
    @Data
    public static class TaskCmd implements Serializable {
        private Long taskId;
        private Cmd cmd;
    }
}
