package com.finance.anubis.repository.impl;

import com.finance.anubis.core.constants.enums.OffLineActivityResultType;
import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.repository.entity.OffLineTaskActivityResultEntity;
import com.finance.anubis.repository.mapper.OffLineTaskActivityResultMapper;
import com.finance.anubis.repository.dto.OffLineTaskActivityResultDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OffLineActivityResultRepositoryImpl implements OffLineActivityResultRepository {

    private final OffLineTaskActivityResultMapper activityResultMapper;

    public OffLineActivityResultRepositoryImpl(OffLineTaskActivityResultMapper activityResultMapper) {
        this.activityResultMapper = activityResultMapper;
    }

    @Override
    public boolean save(OffLineActivityResult activityResult) {
        if (null == activityResult) {
            return false;
        }
        OffLineTaskActivityResultEntity OffLineActivityResultEntity = OffLineTaskActivityResultDTO.toEntity(activityResult);
        LocalDateTime now = LocalDateTime.now();
        OffLineActivityResultEntity.setCreateTime(now);
        OffLineActivityResultEntity.setUpdateTime(now);
        if (activityResultMapper.insert(OffLineActivityResultEntity) == 1) {
            activityResult.setId(OffLineActivityResultEntity.getId());
            return true;
        }

        return false;
    }

    @Override
    public boolean update(OffLineActivityResult activityResult) {
        if (null == activityResult || null == activityResult.getId()) {
            return false;
        }
        OffLineTaskActivityResultEntity OffLineActivityResultEntity = OffLineTaskActivityResultDTO.toEntity(activityResult);
        OffLineActivityResultEntity.setUpdateTime(LocalDateTime.now());
        if (activityResultMapper.updateByPrimaryKey(OffLineActivityResultEntity) == 1) {
            activityResult.setUpdateTime(OffLineActivityResultEntity.getUpdateTime());
            return true;
        }

        return false;
    }

    @Override
    public OffLineActivityResult selectTotalResult(Long id) {
        if (null == id) {
            return null;
        }

        OffLineTaskActivityResultEntity entity = new OffLineTaskActivityResultEntity();
        entity.setTaskActivityId(id);
        entity.setResultType(OffLineActivityResultType.TOTAL);
        OffLineTaskActivityResultEntity resultEntity = activityResultMapper.selectLatestByUniqueInfo(entity);
        return resultEntity == null ? null : OffLineTaskActivityResultDTO.toModel(resultEntity);
    }

    @Override
    public OffLineActivityResult selectDetailResult(Long id) {
        if (null == id) {
            return null;
        }

        OffLineTaskActivityResultEntity entity = new OffLineTaskActivityResultEntity();
        entity.setTaskActivityId(id);
        entity.setResultType(OffLineActivityResultType.DETAIL);
        OffLineTaskActivityResultEntity resultEntity = activityResultMapper.selectLatestByUniqueInfo(entity);
        return resultEntity == null ? null : OffLineTaskActivityResultDTO.toModel(resultEntity);
    }

    @Override
    public List<OffLineActivityResult> selectResultByParam(OffLineActivityResult result) {
        OffLineTaskActivityResultEntity entity = OffLineTaskActivityResultDTO.toEntity(result);
        List<OffLineTaskActivityResultEntity> resultEntities = activityResultMapper.selectByParam(entity);

        return resultEntities.stream().map(e -> OffLineTaskActivityResultDTO.toModel(e)).collect(Collectors.toList());
    }


}
