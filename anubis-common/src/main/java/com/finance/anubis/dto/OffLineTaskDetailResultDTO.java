package com.finance.anubis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 细账对账结果
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OffLineTaskDetailResultDTO extends OffLineTaskResultDTO implements Serializable {

    /**
     * source独有数据
     */
    private Set<String> sourceUnique;

    /**
     * target独有数据
     */
    private Set<String> targetUnique;

    /**
     * 不一致数据
     */
    private Set<String> inConsistency;

    /**
     * source细账对比字段
     */
    private List<String> sourceCompareDetailKeys;

    /**
     * target细账对比字段
     */
    private List<String> targetCompareDetailKeys;


}