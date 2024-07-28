package com.finance.anubis.core.config;

import com.finance.anubis.core.util.DingTalkWebhookUtil;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: linjuanjuan
 * @time: 4/22/22 10:18 AM
 */
@Data
@Configuration
@ConditionalOnExpression("!'${ding.webhook}'.isEmpty()")
@ConfigurationProperties(prefix = "ding.webhook")
public class DingTalkWebhookConfiguration {

    private String url;
    private String secret;

    @Bean
    public DingTalkWebhookUtil dingTalkWebhookUtil() {
        DingTalkWebhookUtil util = new DingTalkWebhookUtil(secret, url);
        return util;
    }

}
