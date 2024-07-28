package com.finance.anubis;

import cn.hutool.json.JSONUtil;
import com.guming.common.biz.nacos.spring.annotation.EnableNacosConfig;
import com.guming.common.redis.annotation.EnableRedis;
import com.guming.fd.distributed.lock.config.EnableDistributedLock;
import com.finance.anubis.core.config.EventResourceConfig;
import com.finance.anubis.core.config.MessageResourceConfig;
import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.context.ActivityContext;
import com.guming.mq.annotation.EnableMQ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * @author longchuan
 * @date 2022-12-28
 */
@EnableNacosConfig
@EnableMQ
@EnableRedis
@SpringBootApplication(scanBasePackages = {"com.finance", "cn.hutool.extra.spring"})
@MapperScan("com.finance.anubis.repository.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@EnableFeignClients(basePackages = {"com.guming.**",})
@EnableDistributedLock(namespace = "anubis")
public class Application {
    static {

        JSONUtil.putDeserializer(MessageResourceConfig.class, (json) -> {
            Object resourceTypeObj = json.getByPath("resourceType");
            if (resourceTypeObj != null) {
                String resourceType = (String) resourceTypeObj;
                if (ResourceType.MessageResourceConfig.getCode().equals(resourceType)) {
                    return json.toBean(MessageResourceConfig.class);
                } else if (ResourceType.EventResourceConfig.getCode().equals(resourceType)) {
                    return json.toBean(EventResourceConfig.class);
                }
            }
            return null;
        });
        JSONUtil.putDeserializer(ActivityContext.SourceContext.class, (json) -> {
            Object resourceTypeObj = json.getByPath("resourceType");
            if (resourceTypeObj != null) {
                String resourceType = (String) resourceTypeObj;
                if (ResourceType.MessageResourceConfig.getCode().equals(resourceType)) {
                    return json.toBean(ActivityContext.MessageSourceContext.class);
                } else if (ResourceType.EventResourceConfig.getCode().equals(resourceType)) {
                    return json.toBean(ActivityContext.EventSourceContext.class);
                }else if (ResourceType.URLResourceConfig.getCode().equals(resourceType)) {
                    return json.toBean(ActivityContext.URLSourceContext.class);
                }
            }
            return null;
        });
//        JSONUtil.putDeserializer(OffLineActivityContext.SourceContext.class, (json) -> {
//            Object resourceTypeObj = json.getByPath("resourceType");
//            if (resourceTypeObj != null) {
//                String resourceType = (String) resourceTypeObj;
//                if (OffLineResourceType.OffLineFileResourceConfig.getCode().equals(resourceType)) {
//                    return json.toBean(OffLineFileResourceConfig.class);
//                } else if (OffLineResourceType.OffLineHttpResourceConfig.getCode().equals(resourceType)) {
//                    return json.toBean(OffLineHttpResourceConfig.class);
//                }
//            }
//            return null;
//        });
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
