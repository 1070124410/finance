package com.finance.anubis.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

/**
 * @Author yezhaoyang
 * @Date 2023/02/21 15:08
 * @Description
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffLineHttpResourceConfig extends OffLineResourceConfig {

    /**
     * 格式化文件映射key的map
     */
    private LinkedHashMap<String, String> jsonFormatMapping;

}
