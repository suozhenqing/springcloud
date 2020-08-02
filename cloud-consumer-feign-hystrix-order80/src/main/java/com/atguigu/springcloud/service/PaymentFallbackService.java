package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService {

    @Override
    public String paymentInfoOK(Integer id) {
        return "PaymentFallbackService ----> paymentInfoOK";
    }

    @Override
    public String paymentInfoTimeOut(Integer id) {
        return "PaymentFallbackService ----> paymentInfoTimeOut";
    }
}
