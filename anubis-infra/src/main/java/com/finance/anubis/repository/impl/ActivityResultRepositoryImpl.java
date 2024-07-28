package com.finance.anubis.repository.impl;

import com.finance.anubis.model.TaskActivityResult;
import com.finance.anubis.repository.ActivityResultRepository;
import com.finance.anubis.repository.dto.TaskActivityResultDTO;
import com.finance.anubis.repository.entity.TaskActivityResultEntity;
import com.finance.anubis.repository.mapper.TaskActivityResultMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ActivityResultRepositoryImpl implements ActivityResultRepository {

    private final TaskActivityResultMapper taskActivityResultMapper;

    public ActivityResultRepositoryImpl(TaskActivityResultMapper taskActivityResultMapper) {
        this.taskActivityResultMapper = taskActivityResultMapper;
    }

    @Override
    public boolean save(TaskActivityResult taskActivityResult) {
        if (null == taskActivityResult) {
            return false;
        }
        TaskActivityResultEntity taskActivityResultEntity = TaskActivityResultDTO.toEntity(taskActivityResult);
        LocalDateTime now = LocalDateTime.now();
        taskActivityResultEntity.setCreateTime(now);
        taskActivityResultEntity.setUpdateTime(now);
        if (taskActivityResultMapper.insert(taskActivityResultEntity) == 1) {
            taskActivityResult.setId(taskActivityResultEntity.getId());
            return true;
        }

        return false;
    }

    @Override
    public boolean update(TaskActivityResult taskActivityResult) {
        if (null == taskActivityResult || null == taskActivityResult.getId()) {
            return false;
        }
        TaskActivityResultEntity taskActivityResultEntity = TaskActivityResultDTO.toEntity(taskActivityResult);
        taskActivityResultEntity.setUpdateTime(LocalDateTime.now());
        if (taskActivityResultMapper.updateByPrimaryKey(taskActivityResultEntity) == 1) {
            taskActivityResult.setUpdateTime(taskActivityResultEntity.getUpdateTime());
            return true;
        }

        return false;
    }


    @Override
    public List<TaskActivityResult> selectByParams(TaskActivityResult taskActivityResult) {
        if (null == taskActivityResult) {
            return Collections.emptyList();
        }
        List<TaskActivityResultEntity> entities = taskActivityResultMapper.selectByParam(TaskActivityResultDTO.toEntity(taskActivityResult));
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }

        return entities.stream().map(TaskActivityResultDTO::toModel).collect(Collectors.toList());
    }

    @Override
    public TaskActivityResult selectByUniqueInfo(TaskActivityResult taskActivityResult) {
        if (null == taskActivityResult.getTaskActivityId() && StringUtils.isBlank(taskActivityResult.getBizKey())) {
            return null;
        }
        TaskActivityResultEntity entity = new TaskActivityResultEntity();
        entity.setBizKey(taskActivityResult.getBizKey());
        entity.setTaskActivityId(taskActivityResult.getTaskActivityId());
        TaskActivityResultEntity taskActivityResultEntity = taskActivityResultMapper.selectLatestByUniqueInfo(entity);
        return taskActivityResultEntity == null ? null : TaskActivityResultDTO.toModel(taskActivityResultEntity);
    }


}
