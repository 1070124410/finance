package com.finance.anubis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OffLineActivityTotalResult extends OffLineActivityResult {

    /**
     * source总账
     */
    private BigDecimal sourceTotal;

    /**
     * target总账
     */
    private BigDecimal targetTotal;

}