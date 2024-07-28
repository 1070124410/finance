package com.finance.anubis.core.config;

import com.finance.anubis.core.constants.enums.MessageInfraType;
import com.finance.anubis.core.constants.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/***
 *
 * @author longchuan
 * @date 2022年12月28日18:13:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResourceConfig extends MessageResourceConfig {

    /**
     * 数据泛化的请求配置
     */

    private List<URLResourceConfig> urlResourceConfigs;

    public EventResourceConfig(
            String group,
            String topic,
            List<String> tags,
            MessageInfraType messageInfraType,
            String messageMode,
            Map<String, String> dataMapping,
            String key,
            List<URLResourceConfig> urlResourceConfigs
    ) {
        super(group, topic, tags, messageInfraType, messageMode, dataMapping, ResourceType.EventResourceConfig, key);
        this.urlResourceConfigs = urlResourceConfigs;
    }
}
