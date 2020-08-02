package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FlowLimitController {

    /**
     * 流控规则：QPS --> 直接 --> 快速失败
     */
    @GetMapping(value = "/testA")
    public String testA() {
        return "FlowLimitController --> testA";
    }
    /**
     * 流控规则：QPS --> 直接 --> 排队等待
     */
    @GetMapping(value = "/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() + "===testB===");
        return "FlowLimitController --> testB";
    }
    /**
     * 降级规则：异常比例/异常数
     */
    @GetMapping(value = "/testC")
    public String testC() {
        log.info(Thread.currentThread().getName() + "===testC===");
        int age = 10 / 0;
        return "FlowLimitController --> testC";
    }
    /**
     * 热点规则：异常比例/异常数,不配置blockHandler则前台打印 Whitelabel Error Page
     * http://localhost:8401/testD?p1=5&p2=p2：参数例外项，5为String类型
     */
    @GetMapping("/testD")
    @SentinelResource(value = "testD", blockHandler = "blockException")
    public String testD(@RequestParam(value = "p1", required = false) String p1,
                        @RequestParam(value = "p2", required = false) String p2) {
        // SentinelResource只处理控制台配置异常，不处理java运行异常，/ by zero 异常输出到页面
        //int age = 10 / 0;
        return "FlowLimitController --> testD";
    }
    public String blockException(String p1, String p2, BlockException exception) {
        return "FlowLimitController --> testD --> blockException";
    }
}