package com.finance.anubis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OffLineTaskTotalResultDTO extends OffLineTaskResultDTO implements Serializable {

    /**
     * source总账
     */
    private BigDecimal sourceTotal;

    /**
     * target总账
     */
    private BigDecimal targetTotal;

    /**
     * source总账对比字段
     */
    private List<String> sourceCompareTotalKeys;

    /**
     * target总账对比字段
     */
    private List<String> targetCompareTotalKeys;

}