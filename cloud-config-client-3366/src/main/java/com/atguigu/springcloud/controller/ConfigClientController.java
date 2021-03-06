package com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 刷新并读取修改后的配置，同时需要发送post：curl -X POST "http://localhost:3355/actuator/refresh"
@RefreshScope
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/configInfo")
    public String getConfigInfo() {

        return "serverPort：" + serverPort + "，configInfo：" + configInfo;
    }
}
