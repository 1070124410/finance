package com.finance.anubis.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 17:55
 * @Description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResourceConfig extends MessageResourceConfig {

    private List<URLResourceConfig> urlResourceConfigs;

}