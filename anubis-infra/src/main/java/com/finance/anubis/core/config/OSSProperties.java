package com.finance.anubis.core.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: linjuanjuan
 * @time: 7/23/21 2:51 PM
 */
@Data
@Configuration
@ConfigurationProperties("oss.client.config")
@ConditionalOnExpression("!'${oss.client.config}'.isEmpty()")
public class OSSProperties {

    private String externalEndPoint;
    private String internalEndPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String fileBucketName;
    private String folder;
    
}
