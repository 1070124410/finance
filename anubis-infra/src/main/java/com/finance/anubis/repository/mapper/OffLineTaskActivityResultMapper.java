package com.finance.anubis.repository.mapper;

import com.finance.anubis.repository.entity.OffLineTaskActivityResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OffLineTaskActivityResultMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OffLineTaskActivityResultEntity record);

    int insertSelective(OffLineTaskActivityResultEntity record);

    OffLineTaskActivityResultEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OffLineTaskActivityResultEntity record);

    int updateByPrimaryKey(OffLineTaskActivityResultEntity record);

    List<OffLineTaskActivityResultEntity> selectPageByParams(@Param("offset") int offset, @Param("size") int size, @Param("entity") OffLineTaskActivityResultEntity taskActivityResultEntity);

    /**
     * 根据参数查询
     * 使用时注意参数判空
     *
     * @param taskActivityResultEntity
     * @return
     */
    List<OffLineTaskActivityResultEntity> selectByParam(@Param("entity") OffLineTaskActivityResultEntity taskActivityResultEntity);

    OffLineTaskActivityResultEntity selectLatestByUniqueInfo(@Param("entity") OffLineTaskActivityResultEntity taskActivityResultEntity);

    List<OffLineTaskActivityResultEntity> selectLatestByParam(@Param("entity") OffLineTaskActivityResultEntity taskActivityResultEntity);
}