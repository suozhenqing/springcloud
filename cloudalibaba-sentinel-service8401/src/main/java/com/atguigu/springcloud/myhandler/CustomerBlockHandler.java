package com.atguigu.springcloud.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;

public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException exception) {
        return new CommonResult(445, "按照客户自定义 global handlerException");
    }
    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(446, "按照客户自定义 global handlerException2");
    }
}
