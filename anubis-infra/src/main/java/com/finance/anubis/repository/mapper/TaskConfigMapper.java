package com.finance.anubis.repository.mapper;

import com.finance.anubis.repository.entity.TaskConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskConfigMapper {
    int insert(TaskConfigEntity taskConfigEntity);
    int updateByPrimaryKeySelective(TaskConfigEntity taskConfigEntity);

    int updateByPrimaryKey(TaskConfigEntity taskConfigEntity);

    TaskConfigEntity selectByPrimaryKey(Long id);

    List<TaskConfigEntity> selectByParams(TaskConfigEntity taskConfig);

    TaskConfigEntity selectByTaskName(String taskName);

    List<TaskConfigEntity> selectListByIds(@Param("configIds")List<Long> configIds);

    int deleteByPrimaryKey(Long id);
}
