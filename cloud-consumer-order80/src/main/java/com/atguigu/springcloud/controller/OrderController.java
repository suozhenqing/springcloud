package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {
    // 单机版
    // public static final String PAYMENT_RUL = "http://localhost:8001";

    // 集群配置，需要开启RestTemplate的负载均衡配置
    public static final String PAYMENT_RUL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_RUL + "/payment/create", payment, CommonResult.class);
    }
    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_RUL + "/payment/get/" + id, CommonResult.class);
    }
    @GetMapping(value = "/consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_RUL + "/payment/get/" + id, CommonResult.class);

        log.info("getForEntity：" + entity.getHeaders().toString());
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "操作失败");
        }
    }
    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (null == instances || instances.size() == 0) {
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        URI uri = serviceInstance.getUri();
        System.out.println("uri ----> " + uri);
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }
    // 链路监控测试调用
    @GetMapping(value = "/consumer/payment/zipkin")
    public String paymentZipkin() {
        return restTemplate.getForObject("http://localhost:8001" + "/payment/zipkin", String.class);
    }
}
