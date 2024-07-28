package com.finance.anubis.core.config;

import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.context.ActivityContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author longchuan
 * @data 2022年12月28日18:11:19
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class URLResourceConfig extends ResourceConfig {

    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式，目前支持get、post
     */
    private String method;


    /**
     * 请求参数映射
     */
    private Map<String, ActivityContext.ParamPath> requestParamMapping;


    public URLResourceConfig(String url,
                             String method,
                             Map<String, ActivityContext.ParamPath> requestParamMapping,
                             Map<String, String> responseDataMap,
                             ResourceType resourceType,
                             String key) {
        super(resourceType, responseDataMap, key);
        this.url = url;
        this.method=method;
        this.requestParamMapping = requestParamMapping;
    }
}
