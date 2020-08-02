package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "---OK---" + id;
    }
    @HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
    public String paymentInfoTimeOut(Integer id) {
        int time = 3;
        //int age = 10 / 0;
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "---TimeOut---" + id;
    }
    public String paymentInfoTimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "---paymentInfoTimeOutHandler---" + id;
    }

    //-----服务熔断 ---> 服务降级/服务熔断/恢复调用链路
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",
            commandProperties = {@HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),//请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),// 时间范围
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
            })
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****id 不能负数");
        }
        // https://hutool.cn/docs/#/
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功,流水号：" + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(Integer id) {
        return "id 不能负数，请稍候再试,(┬＿┬)/~~ id: " + id;
    }
}
