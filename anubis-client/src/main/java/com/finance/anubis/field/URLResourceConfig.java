package com.finance.anubis.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:38
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class URLResourceConfig extends ResourceConfig {
    /**
     * 请求地址
     */
    private String url;

    private String method;
    /**
     * 请求参数映射
     */
    private Map<String, ParamPath> requestParamMapping;

    @Data
    public static class ParamPath implements Serializable {
        private String sourceKey;
        private String path;
    }
}
