package com.finance.anubis.repository.impl;

import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.task.model.TaskActivity;
import com.finance.anubis.exception.StatusCodeEnum;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.repository.dto.TaskActivityDTO;
import com.finance.anubis.repository.entity.TaskActivityEntity;
import com.finance.anubis.repository.entity.TaskConfigEntity;
import com.finance.anubis.repository.mapper.TaskActivityMapper;
import com.finance.anubis.repository.mapper.TaskConfigMapper;
import com.guming.api.pojo.page.Limit;
import com.guming.common.exception.StatusCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TaskActivityRepositoryImpl implements TaskActivityRepository {


    private final TaskActivityMapper taskActivityMapper;
    private final TaskConfigMapper taskConfigMapper;

    public TaskActivityRepositoryImpl(TaskActivityMapper taskActivityMapper, TaskConfigMapper taskConfigMapper) {
        this.taskActivityMapper = taskActivityMapper;
        this.taskConfigMapper = taskConfigMapper;
    }

    @Override
    public boolean save(TaskActivity taskActivity) {
        if (null == taskActivity) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = TaskActivityDTO.toEntity(taskActivity);
        LocalDateTime now = LocalDateTime.now();
        taskActivityEntity.setCreateTime(now);
        taskActivityEntity.setUpdateTime(now);
        boolean success = taskActivityMapper.insert(taskActivityEntity) == 1;
        if (success) {
            taskActivity.setId(taskActivityEntity.getId());
        }
        return success;
    }

    @Override
    public boolean update(TaskActivity taskActivity) {
        if (null == taskActivity) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = TaskActivityDTO.toEntity(taskActivity);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        boolean success = taskActivityMapper.updateByPrimaryKey(taskActivityEntity) == 1;
        if (success) {
            taskActivity.setUpdateTime(taskActivityEntity.getUpdateTime());
        }
        return success;
    }

    @Override
    public boolean updateAction(Long id, Action action) {
        TaskActivityEntity entity=new TaskActivityEntity();
        entity.setId(id);
        entity.setAction(action);

        return taskActivityMapper.updateByPrimaryKey(entity) == 1;
    }

    @Override
    public TaskActivity getById(Long taskActivityId) {
        TaskActivityEntity taskActivityEntity = taskActivityMapper.selectByPrimaryKey(taskActivityId);
        if (taskActivityEntity == null) {
            return null;
        }
        TaskConfigEntity taskConfigEntity = taskConfigMapper.selectByPrimaryKey(taskActivityEntity.getTaskConfigId());
        return TaskActivityDTO.toModel(taskActivityEntity, taskConfigEntity);
    }

    @Override
    public TaskActivity getByBizKey(String bizKey) {
        if (StringUtils.isBlank(bizKey)) {
            return null;
        }
        TaskActivityEntity taskActivityEntity = taskActivityMapper.selectByBizKey(bizKey);
        if (taskActivityEntity == null) {
            return null;
        }
        TaskConfigEntity taskConfigEntity = taskConfigMapper.selectByPrimaryKey(taskActivityEntity.getTaskConfigId());
        return TaskActivityDTO.toModel(taskActivityEntity, taskConfigEntity);
    }

    @Override
    public List<TaskActivity> getByParams(TaskActivity activity) {
        if (null == activity) {
            return Collections.emptyList();
        }
        TaskActivityEntity entity = new TaskActivityEntity();
        entity.setId(activity.getId());
        entity.setAction(activity.getAction());
        List<TaskActivityEntity> entities = taskActivityMapper.selectByParam(entity);
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        //key:taskActivityId value:taskConfigId
        Map<Long, Long> idMap = entities.stream().collect(Collectors.toMap(TaskActivityEntity::getId, TaskActivityEntity::getTaskConfigId));
        //key:taskConfigId value:taskConfig
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigMapper.selectListByIds(new ArrayList<>(idMap.values())).stream().collect(Collectors.toMap(TaskConfigEntity::getId, a -> a));

        return entities.stream().map(e -> TaskActivityDTO.toModel(e, configEntityMap.get(idMap.get(e.getId())))).collect(Collectors.toList());
    }

    @Override
    public List<TaskActivity> getPageByParams(Limit page, TaskActivity activity) {
        if (null == activity) {
            return Collections.emptyList();
        }
        TaskActivityEntity entity = new TaskActivityEntity();
        entity.setAction(activity.getAction());
        List<TaskActivityEntity> entities = taskActivityMapper.selectPageByParams(page.getOffset(), page.getSize(), entity);
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        //key:taskActivityId value:taskConfigId
        Map<Long, Long> idMap = entities.stream().collect(Collectors.toMap(TaskActivityEntity::getId, TaskActivityEntity::getTaskConfigId));
        //key:taskConfigId value:taskConfig
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigMapper.selectListByIds(new ArrayList<>(idMap.values())).stream().collect(Collectors.toMap(TaskConfigEntity::getId, a -> a));

        return entities.stream().map(e -> TaskActivityDTO.toModel(e, configEntityMap.get(idMap.get(e.getId())))).collect(Collectors.toList());
    }
}
