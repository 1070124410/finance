package com.finance.anubis.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.enums.OffLineActivityResultType;
import com.finance.anubis.enums.OffLineActivityVerifyResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 19:33
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
                value = OffLineTaskTotalResultDTO.class,
                name = "TOTAL"),
        @JsonSubTypes.Type(
                value = OffLineTaskDetailResultDTO.class,
                name = "DETAIL")})
public class OffLineTaskResultDTO implements Serializable {

    /**
     * 对账结果类型
     */
    private OffLineActivityResultType resultType;

    /**
     * 对账结果
     */
    private OffLineActivityVerifyResult verifyResult;
}
