package com.finance.anubis.repository.mapper;

import com.finance.anubis.repository.entity.TaskActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskActivityEntity record);

    int insertSelective(TaskActivityEntity record);

    TaskActivityEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskActivityEntity record);

    int updateByPrimaryKey(TaskActivityEntity record);

    List<TaskActivityEntity> selectPageByParams(@Param("offset") int offset, @Param("size") int size, @Param("entity") TaskActivityEntity taskActivityEntity);

    /**
     * 根据参数查询
     * 使用时注意参数判空
     *
     * @param taskActivityEntity
     * @return
     */
    List<TaskActivityEntity> selectByParam(@Param("entity") TaskActivityEntity taskActivityEntity);

    TaskActivityEntity selectByBizKey(String bizKey);

}