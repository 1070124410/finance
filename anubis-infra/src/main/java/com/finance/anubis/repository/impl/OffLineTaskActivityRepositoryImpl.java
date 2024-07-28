package com.finance.anubis.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.enums.OffLineAction;
import com.finance.anubis.exception.StatusCodeEnum;
import com.finance.anubis.exception.StatusCodeException;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.dto.TaskActivityDTO;
import com.finance.anubis.repository.entity.TaskActivityEntity;
import com.finance.anubis.repository.entity.TaskConfigEntity;
import com.finance.anubis.repository.mapper.OffLineTaskActivityMapper;
import com.finance.anubis.repository.mapper.TaskConfigMapper;
import com.finance.anubis.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OffLineTaskActivityRepositoryImpl implements OffLineTaskActivityRepository {


    private final OffLineTaskActivityMapper taskActivityMapper;
    private final TaskConfigMapper taskConfigMapper;

    public OffLineTaskActivityRepositoryImpl(OffLineTaskActivityMapper taskActivityMapper, TaskConfigMapper taskConfigMapper) {
        this.taskActivityMapper = taskActivityMapper;
        this.taskConfigMapper = taskConfigMapper;
    }

    @Override
    public boolean save(OffLineTaskActivity taskActivity) {
        if (null == taskActivity) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = TaskActivityDTO.toOffLineEntity(taskActivity);
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
    public boolean update(OffLineTaskActivity taskActivity) {
        if (null == taskActivity) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = TaskActivityDTO.toOffLineEntity(taskActivity);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        boolean success = taskActivityMapper.updateByPrimaryKey(taskActivityEntity) == 1;
        if (success) {
            taskActivity.setUpdateTime(taskActivityEntity.getUpdateTime());
        }
        return success;
    }

    @Override
    public OffLineTaskActivity getByBizKey(String bizKey) {
        if (StringUtils.isBlank(bizKey)) {
            return null;
        }
        TaskActivityEntity taskActivityEntity = taskActivityMapper.selectByBizKey(bizKey);
        if (taskActivityEntity == null) {
            return null;
        }
        TaskConfigEntity taskConfigEntity = taskConfigMapper.selectByPrimaryKey(taskActivityEntity.getTaskConfigId());
        return TaskActivityDTO.toOffLineModel(taskActivityEntity, taskConfigEntity);
    }

    @Override
    public boolean toDataFetch(Long id, OffLineActivityContext context, OffLineAction action) {
        if (null == id || null == action) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        if (!OffLineAction.DATA_FETCH.originAction().contains(action)) {
            return false;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setContext(JsonUtil.toJson(context));
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityEntity.setOffLineAction(OffLineAction.DATA_FETCH);
        return taskActivityMapper.updateOffLineAction(taskActivityEntity) == 1;
    }

    @Override
    public boolean toFail(Long id, OffLineAction action) {
        if (null == id || null == action) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        if (!OffLineAction.DATA_FAIL.originAction().contains(action)) {
            return false;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityEntity.setOffLineAction(OffLineAction.DATA_FAIL);
        return taskActivityMapper.updateOffLineAction(taskActivityEntity) == 1;
    }

    @Override
    public boolean toDataCompareDetail(Long id, OffLineAction action) {
        if (null == id || null == action) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        if (!OffLineAction.DATA_COMPARE_DETAIL.originAction().contains(action)) {
            return false;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityEntity.setOffLineAction(OffLineAction.DATA_COMPARE_DETAIL);
        return taskActivityMapper.updateOffLineAction(taskActivityEntity) == 1;
    }

    @Override
    public boolean toDataCompareTotal(Long id, OffLineAction action) {
        if (null == id || null == action) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        if (!OffLineAction.DATA_COMPARE_TOTAL.originAction().contains(action)) {
            return false;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityEntity.setOffLineAction(OffLineAction.DATA_COMPARE_TOTAL);
        return taskActivityMapper.updateOffLineAction(taskActivityEntity) == 1;
    }

    @Override
    public boolean toDataDone(Long id, OffLineAction action) {
        if (null == id || null == action) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        if (!OffLineAction.DATA_DONE.originAction().contains(action)) {
            return false;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityEntity.setOffLineAction(OffLineAction.DATA_DONE);
        return taskActivityMapper.updateOffLineAction(taskActivityEntity) == 1;
    }

    @Override
    public boolean updateTimes(Long id) {
        if (null == id) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);

        return taskActivityMapper.updateTimes(taskActivityEntity) == 1;
    }

    @Override
    public boolean updateContext(Long id, OffLineActivityContext context) {
        if (null == id || null == context) {
            throw new StatusCodeException(StatusCodeEnum.ToStatus(StatusCodeEnum.NullParam));
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setContext(JsonUtil.toJson(context));
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        return taskActivityMapper.updateByPrimaryKeySelective(taskActivityEntity) == 1;
    }

    @Override
    public List<OffLineTaskActivity> selectActiveList() {
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        //获取当天00:00
        taskActivityEntity.setUpdateTime(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN));
        List<TaskActivityEntity> entities = taskActivityMapper.selectActiveActivity(taskActivityEntity);
        if (CollUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }
        Map<Long, Long> idMap = entities.stream().collect(Collectors.toMap(TaskActivityEntity::getId, TaskActivityEntity::getTaskConfigId));
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigMapper.selectListByIds(new ArrayList<>(idMap.values())).stream().collect(Collectors.toMap(TaskConfigEntity::getId, a -> a));

        return entities.stream().map(e -> TaskActivityDTO.toOffLineModel(e, configEntityMap.get(idMap.get(e.getId())))).collect(Collectors.toList());
    }

    @Override
    public void resetContext(Long id, OffLineActivityContext context) {
        if (id == null) {
            return;
        }
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(id);
        taskActivityEntity.setContext(JsonUtil.toJson(context));
        taskActivityEntity.setUpdateTime(LocalDateTime.now());
        taskActivityMapper.updateByPrimaryKeySelective(taskActivityEntity);
    }

    @Override
    public boolean updateAction(Long id, OffLineAction action) {
        TaskActivityEntity entity = new TaskActivityEntity();
        entity.setId(id);
        entity.setOffLineAction(action);

        return taskActivityMapper.updateByPrimaryKeySelective(entity) == 1;
    }

    @Override
    public OffLineTaskActivity getById(Long taskActivityId) {
        if (null == taskActivityId) {
            return null;
        }
        TaskActivityEntity taskActivityEntity = taskActivityMapper.selectByPrimaryKey(taskActivityId);
        if (taskActivityEntity == null) {
            return null;
        }
        TaskConfigEntity taskConfigEntity = taskConfigMapper.selectByPrimaryKey(taskActivityEntity.getTaskConfigId());
        return TaskActivityDTO.toOffLineModel(taskActivityEntity, taskConfigEntity);
    }

    @Override
    public List<OffLineTaskActivity> getByParams(OffLineTaskActivity activity) {
        if (null == activity) {
            return Collections.emptyList();
        }
        TaskActivityEntity entity = new TaskActivityEntity();
        entity.setId(activity.getId());
        entity.setOffLineAction(activity.getAction());
        List<TaskActivityEntity> entities = taskActivityMapper.selectByParam(entity);
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        //key:taskActivityId value:taskConfigId
        Map<Long, Long> idMap = entities.stream().collect(Collectors.toMap(TaskActivityEntity::getId, TaskActivityEntity::getTaskConfigId));
        //key:taskConfigId value:taskConfig
        Map<Long, TaskConfigEntity> configEntityMap = taskConfigMapper.selectListByIds(new ArrayList<>(idMap.values())).stream().collect(Collectors.toMap(TaskConfigEntity::getId, a -> a));

        return entities.stream().map(e -> TaskActivityDTO.toOffLineModel(e, configEntityMap.get(idMap.get(e.getId())))).collect(Collectors.toList());
    }

}
