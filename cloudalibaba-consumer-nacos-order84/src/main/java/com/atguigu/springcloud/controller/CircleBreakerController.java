package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
public class CircleBreakerController {

    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/fallBack/{id}")
    // 返回运行时异常
    //@SentinelResource(value = "fallBack")

    // 返回handlerFallBack兜底异常
    //@SentinelResource(value = "fallBack", fallback = "handlerFallBack")

    // 只有满足sentinel配置时才返回blockHandler控制异常，否则返回运行时异常
    //@SentinelResource(value = "fallBack", blockHandler = "blockHandler")

    // id=1,快速点击命中blockHandler; id=4,1QPS命中handlerFallBack，快速点击命中blockHandler
    @SentinelResource(value = "fallBack", fallback = "handlerFallBack",
            blockHandler = "blockHandler",
            exceptionsToIgnore = {IllegalArgumentException.class})
    public CommonResult<Payment> fallBack(@PathVariable("id") Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);

        if (4 == id) {
            throw new IllegalArgumentException("非法参数异常");
        } else if (null == result.getData()) {
            throw new NullPointerException("空指针异常");
        }
        return result;
    }
    public CommonResult handlerFallBack(Long id, Throwable e) {
        Payment payment = new Payment(id, null);
        return new CommonResult(444, "兜底异常handlerFallBack，exception --> " + e.getMessage(), payment);
    }

    public CommonResult blockHandler(Long id, BlockException e) {
        Payment payment = new Payment(id, null);
        return new CommonResult(445, "SentinelResource --> blockHandler，exception --> " + e.getClass().getCanonicalName(), payment);
    }


    //---------------openfeign
    @Resource
    private PaymentService paymentService;

    @GetMapping(value = "/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        return paymentService.paymentSQL(id);
    }
 }
