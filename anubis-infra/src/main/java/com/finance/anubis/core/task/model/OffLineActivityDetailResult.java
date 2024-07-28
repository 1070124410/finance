package com.finance.anubis.core.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OffLineActivityDetailResult extends OffLineActivityResult {

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



}