package com.finance.anubis.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.enums.ActionResult;
import com.finance.anubis.enums.OffLineActivityResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author yezhaoyang
 * @Date 2023/03/08 19:46
 * @Description
 **/
@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "resultType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = OffLineActivityTotalResult.class,
                name = "TOTAL"),
        @JsonSubTypes.Type(
                value = OffLineActivityDetailResult.class,
                name = "DETAIL")})
public class OffLineActivityResult extends BaseModel {

    private Long taskActivityId;

    private String bizKey;

    private Integer times;

    /**
     * 对账结果类型
     */
    private OffLineActivityResultType resultType;

    /**
     * 对账结果
     */
    private ActionResult verifyResult;

    /**
     * 比较字段
     */
    private ResultKey compareKeys;

    /**
     * 对账结果数据
     */
    private ResultInfo compareData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResultKey {

        /**
         * source对账字段
         */
        private List<String> sourceCompareKeys;

        /**
         * target对账字段
         */
        private List<String> targetCompareKeys;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "resultInfoType",
            visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(
                    value = TotalResultInfo.class,
                    name = "TOTAL"),
            @JsonSubTypes.Type(
                    value = DetailResultInfo.class,
                    name = "DETAIL")})
    public static class ResultInfo {

        protected OffLineActivityResultType resultInfoType;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class TotalResultInfo extends ResultInfo {

        /**
         * source总账
         */
        private BigDecimal sourceTotal;

        /**
         * target总账
         */
        private BigDecimal targetTotal;

        public TotalResultInfo(BigDecimal sourceTotal, BigDecimal targetTotal) {
            super(OffLineActivityResultType.TOTAL);
            this.sourceTotal = sourceTotal;
            this.targetTotal = targetTotal;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class DetailResultInfo extends ResultInfo {

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

        public DetailResultInfo(Set<String> sourceUnique, Set<String> targetUnique, Set<String> inConsistency) {
            super(OffLineActivityResultType.DETAIL);
            this.sourceUnique = sourceUnique;
            this.targetUnique = targetUnique;
            this.inConsistency = inConsistency;
        }

    }

}
