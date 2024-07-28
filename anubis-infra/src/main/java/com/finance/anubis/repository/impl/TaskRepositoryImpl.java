package com.finance.anubis.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.finance.anubis.core.constants.enums.TaskStatus;
import com.finance.anubis.core.constants.enums.TaskType;
import com.finance.anubis.core.task.model.Task;
import com.finance.anubis.exception.StatusCodeEnum;
import com.finance.anubis.repository.TaskRepository;
import com.finance.anubis.repository.dto.TaskConfigDTO;
import com.finance.anubis.repository.dto.TaskDTO;
import com.finance.anubis.repository.entity.TaskConfigEntity;
import com.finance.anubis.repository.entity.TaskEntity;
import com.finance.anubis.repository.mapper.TaskConfigMapper;
import com.finance.anubis.repository.mapper.TaskMapper;
import com.guming.api.pojo.page.Limit;
import com.guming.common.exception.StatusCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskMapper taskMapper;

    private final TaskConfigMapper taskConfigMapper;

    public TaskRepositoryImpl(TaskMapper taskMapper, TaskConfigMapper taskConfigMapper) {
        this.taskMapper = taskMapper;
        this.taskConfigMapper = taskConfigMapper;
    }

    @Override
    public Task selectById(Long id) {
        TaskEntity taskEntity = taskMapper.selectByPrimaryKey(id);
        if (null == taskEntity) {
            return null;
        }
        Long configId = taskEntity.getTaskConfigId();
        TaskConfigEntity taskConfigEntity = selectConfigById(configId);
        if (null == taskConfigEntity) {
            log.error("taskConfig not exist,taskId:{}", id);
            throw new RuntimeException();
        }

        return TaskDTO.toModel(taskEntity, taskConfigEntity);
    }

    @Override
    public List<Task> selectTaskPage(Limit page, Task task) {
        if (null == task) {
            return Collections.emptyList();
        }
        List<TaskEntity> taskEntities = taskMapper.selectPagesByParams(page.getOffset(), page.getSize(), TaskDTO.toEntity(task));
        if (CollUtil.isEmpty(taskEntities)) {
            return Collections.emptyList();
        }
        List<Long> taskConfigIds = taskEntities.stream().map(TaskEntity::getTaskConfigId).collect(Collectors.toList());

        List<TaskConfigEntity> taskConfigEntities = selectConfigListByIds(taskConfigIds);
        if (taskConfigEntities.size() != taskConfigIds.size()) {
            log.error("#selectTaskPage,config lack,configIds:{}", taskConfigIds);
            throw new RuntimeException();
        }
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigEntities.stream().collect(Collectors.toMap(TaskConfigEntity::getId, e -> e));

        return taskEntities.stream().map(e -> TaskDTO.toModel(e, configEntityMap.get(e.getTaskConfigId()))).collect(Collectors.toList());
    }

    @Override
    public List<Task> selectTaskByParam(TaskEntity taskEntity) {
        if (null == taskEntity) {
            return Collections.emptyList();
        }
        List<TaskEntity> entityList = taskMapper.selectByParams(taskEntity);
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        //key:taskId value:taskConfigId
        Map<Long, Long> idMap = entityList.stream().collect(Collectors.toMap(TaskEntity::getId, TaskEntity::getTaskConfigId));
        //key:taskConfigId value:TaskConfigEntity
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigMapper.selectListByIds(new ArrayList<>(idMap.values())).stream().collect(Collectors.toMap(TaskConfigEntity::getId, a -> a));
        if (configEntityMap.size() != entityList.size()) {
            log.error("#selectList config lack,ids:{}", idMap.values());
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.IllegalResult));
        }

        return entityList.stream().map(entity -> TaskDTO.toModel(entity, configEntityMap.get(idMap.get(entity.getId())))).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = StatusCodeException.class)
    public Task add(Task task) {
        TaskConfigEntity taskConfigEntity = null;
        taskConfigEntity = addConfig(TaskConfigDTO.toEntity(task.getTaskConfig()));
        TaskEntity taskEntity = TaskDTO.toEntity(task);
        LocalDateTime now = LocalDateTime.now();
        taskEntity.setCreateTime(now);
        taskEntity.setUpdateTime(now);
        taskEntity.setTaskConfigId(taskConfigEntity.getId());
        try {
            if (taskMapper.insert(taskEntity) == 0) {
                throw new RuntimeException();
            }
        } catch (DuplicateKeyException e) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.DuplicateKey));
        } catch (Exception e) {
            log.error("addTask error,task:{},", task, e);
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.InsertFail));
        }

        return TaskDTO.toModel(taskEntity, taskConfigEntity);
    }

    @Override
    @Transactional(rollbackFor = StatusCodeException.class)
    public Task updateTask(Task task) {
        TaskConfigEntity taskConfigEntity = null;
        taskConfigEntity = updateConfig(TaskConfigDTO.toEntity(task.getTaskConfig()));
        TaskEntity taskEntity = TaskDTO.toEntity(task);
        taskEntity.setUpdateTime(LocalDateTime.now());
        try {
            if (taskMapper.updateByPrimaryKeySelective(taskEntity) == 0) {
                throw new RuntimeException();
            }
        } catch (DuplicateKeyException e) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.DuplicateKey));
        } catch (Exception e) {
            log.error("#updateTask error,task:{},", task, e);
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.UpdateFail));
        }

        return TaskDTO.toModel(taskEntity, taskConfigEntity);
    }


    @Override
    public boolean start(Long taskId) {
        TaskEntity taskEntity = taskMapper.selectByPrimaryKey(taskId);
        if (taskEntity != null) {
            taskEntity.setStatus(TaskStatus.START);
            taskEntity.setUpdateTime(LocalDateTime.now());
            return taskMapper.updateByPrimaryKeySelective(taskEntity) == 1;
        }
        return false;
    }


    @Override
    public boolean stop(Long taskId) {
        TaskEntity taskEntity = taskMapper.selectByPrimaryKey(taskId);
        if (taskEntity != null) {
            taskEntity.setStatus(TaskStatus.STOP);
            taskEntity.setUpdateTime(LocalDateTime.now());
            return taskMapper.updateByPrimaryKeySelective(taskEntity) == 1;
        }
        return false;
    }

    @Override
    public List<Task> activeTaskList() {
        List<TaskEntity> taskEntities = taskMapper.listAllByStatus(TaskStatus.START, TaskType.ONLINE);
        Map<Long, TaskEntity> taskEntityMap = taskEntities.stream().collect(Collectors.toMap(TaskEntity::getTaskConfigId, taskEntity -> taskEntity));
        List<Long> taskConfigIds = taskEntities.stream().map(TaskEntity::getTaskConfigId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(taskConfigIds)) {
            List<TaskConfigEntity> taskConfigEntities = taskConfigMapper.selectListByIds(taskConfigIds);
            return taskConfigEntities.stream().map(taskConfigEntity -> {
                TaskEntity taskEntity = taskEntityMap.get(taskConfigEntity.getId());
                return TaskDTO.toModel(taskEntity, taskConfigEntity);
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    @Override
    public Task selectByName(String taskName) {
        if (StringUtils.isBlank(taskName)) {
            return null;
        }
        TaskConfigEntity configEntity = taskConfigMapper.selectByTaskName(taskName);
        if (configEntity == null) {
            return null;
        }
        TaskEntity taskEntity = taskMapper.selectByTaskConfigId(configEntity.getId());

        return TaskDTO.toModel(taskEntity, configEntity);
    }


    public TaskConfigEntity selectConfigById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }

        return taskConfigMapper.selectByPrimaryKey(id);
    }

    public List<TaskConfigEntity> selectConfigListByIds(List<Long> taskConfigIds) {
        if (ObjectUtils.isEmpty(taskConfigIds)) {
            return Collections.emptyList();
        }
        return taskConfigMapper.selectListByIds(taskConfigIds);
    }

    public TaskConfigEntity addConfig(TaskConfigEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setVersion(0);
        try {
            if (taskConfigMapper.insert(entity) == 0) {
                throw new StatusCodeException((StatusCodeEnum.ToStatus(StatusCodeEnum.InsertFail)));
            }
        } catch (DuplicateKeyException e) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.DuplicateKey));
        } catch (StatusCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error("#addConfig error,config:{},", entity, e);
            throw new StatusCodeException((StatusCodeEnum.ToStatus(StatusCodeEnum.UNKNOWN)));
        }
        return entity;
    }

    public TaskConfigEntity updateConfig(TaskConfigEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        try {
            if (taskConfigMapper.updateByPrimaryKeySelective(entity) == 0) {
                throw new StatusCodeException((StatusCodeEnum.ToStatus(StatusCodeEnum.UpdateFail)));
            }
        } catch (DuplicateKeyException e) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.DuplicateKey));
        } catch (StatusCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error("#updateTaskConfig error,config:{},", entity, e);
            throw new StatusCodeException((StatusCodeEnum.ToStatus(StatusCodeEnum.UNKNOWN)));
        }
        return entity;
    }
}
