package com.finance.anubis.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yezhaoyang
 * @Date 2023/03/01 10:46
 * @Description
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "resourceType", visible =
        true)
@JsonSubTypes({@JsonSubTypes.Type(value = OffLineHttpResourceConfig.class, name = "OffLineHttpResource"),
        @JsonSubTypes.Type(value = OffLineFileResourceConfig.class, name = "OffLineFileResource")})
public abstract class OffLineResourceConfig {

    /**
     * 任务配置key
     */
    private String key;
    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式，目前支持get、post
     */
    private String method;

    /**
     * 请求参数
     */
    private Map<String, String> requestParamMapping;

    /**
     * 对账资源类型：文件、接口
     */
    private String resourceType;

    /**
     * fetch对账数据允许的超时时长
     */
    private Long fetchDelay;

    /**
     * 文件排序时所需要的key的map,key为排序字段名，value为顺序倒序
     */
    private LinkedHashMap<String, String> sortKeyMap;

    /**
     * 对总账所需要的key
     */
    private List<String> compareTotalKeys;

    /**
     * 计算总账的表达式
     */
    private String computeExpressions;

    /**
     * 对细账所需要的key
     */
    private List<String> compareDetailKeys;

}
