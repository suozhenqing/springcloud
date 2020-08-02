package com.atguigu.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("MyLogGateWayFilter" + new Date().toString());
        String name = exchange.getRequest().getQueryParams().getFirst("name");
        if (null == name || "".equals(name)) {
            log.info("*********用户名为空");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        } else {
            log.info("*********用户名为：" + name);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {

        return 0;
    }
}
