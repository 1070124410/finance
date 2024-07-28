package com.finance.anubis.repository.mapper;

import com.finance.anubis.core.constants.enums.TaskStatus;
import com.finance.anubis.core.constants.enums.TaskType;
import com.finance.anubis.repository.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
    int insert(TaskEntity taskEntity);

    int insertSelective(TaskEntity taskEntity);

    int updateByPrimaryKey(TaskEntity taskEntity);

    int updateByPrimaryKeySelective(TaskEntity taskEntity);

    TaskEntity selectByPrimaryKey(long taskId);

    List<TaskEntity> selectListByIds(@Param("taskIds") List<Long> ids);

    List<TaskEntity> selectPagesByParams(@Param("offset")int offset,@Param("size")int size, @Param("task")TaskEntity taskEntity);

    List<TaskEntity> selectByParams(TaskEntity taskEntity);

    TaskEntity selectByTaskConfigId(Long id);


    int deleteByPrimaryKey(Long id);


    List<TaskEntity>  listAllByStatus(@Param("status") TaskStatus taskStatus, @Param("type") TaskType taskType);
}
