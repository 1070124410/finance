package com.finance.anubis.repository.mapper;

import com.finance.anubis.repository.entity.TaskActivityResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskActivityResultMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskActivityResultEntity record);

    int insertSelective(TaskActivityResultEntity record);

    TaskActivityResultEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskActivityResultEntity record);

    int updateByPrimaryKey(TaskActivityResultEntity record);

    List<TaskActivityResultEntity> selectPageByParams(@Param("offset") int offset, @Param("size") int size, @Param("entity") TaskActivityResultEntity taskActivityResultEntity);

    /**
     * 根据参数查询
     * 使用时注意参数判空
     *
     * @param taskActivityResultEntity
     * @return
     */
    List<TaskActivityResultEntity> selectByParam(@Param("entity") TaskActivityResultEntity taskActivityResultEntity);

    TaskActivityResultEntity selectLatestByUniqueInfo(@Param("entity") TaskActivityResultEntity taskActivityResultEntity);
}