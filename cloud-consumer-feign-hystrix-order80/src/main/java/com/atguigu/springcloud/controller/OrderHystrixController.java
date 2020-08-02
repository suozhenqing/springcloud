package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfoOK(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfoOK(id);
    }

    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    //@HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler",
    //        commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")})
    @HystrixCommand
    public String paymentInfoTimeOut(@PathVariable("id") Integer id) {

        int age = 10 /0;
        return paymentHystrixService.paymentInfoTimeOut(id);
    }
    public String paymentInfoTimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "  cousumer---paymentInfoTimeOutHandler---" + id;
    }
    // 全局fallback方法
    public String payment_Global_FallbackMethod() {
        return "Global异常处理信息，请稍后再试,(┬＿┬)";
    }
}
