package com.finance.anubis.exception;

import com.guming.api.pojo.Status;
import com.finance.anubis.core.constants.enums.EnumBase;

/**
 * @Author yezhaoyang
 * @Date 2023/01/17 17:17
 * @Description 状态码枚举类
 **/
public enum StatusCodeEnum implements EnumBase<Integer> {

    Common(4000, "通用异常"),
    PersistenceException(4001, "mybatis解析异常"),
    DuplicateKey(4002, "唯一索引重复异常"),
    NullParam(4003, "空参数异常"),
    IllegalParam(4004, "非法参数"),
    IllegalResult(4005, "非法结果"),
    ParseError(4006, "解析异常"),
    UNKNOWN(4007,"未知异常"),
    InsertFail(4101, "添加失败"),
    UpdateFail(4102, "更新失败"),
    DeleteFail(4103, "删除失败"),
    TaskNotExist(5001, "任务不存在"),
    TaskNotComplete(5001, "任务信息不完整"),
    ;


    private Integer code;
    private String message;

    StatusCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


    public static Status ToStatus(StatusCodeEnum enumEnum) {
        for (StatusCodeEnum e : StatusCodeEnum.values()) {
            if (e.getCode().equals(enumEnum.getCode())) {
                return Status.create(e.getCode(), e.getMessage());
            }
        }
        return Status.create(Common.getCode(), Common.getMessage());
    }
}
