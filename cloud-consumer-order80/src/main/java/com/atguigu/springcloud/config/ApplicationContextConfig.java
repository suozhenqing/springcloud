package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    /**
     * @LoadBalanced 默认轮询方式
     */
    @Bean
    //@LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
