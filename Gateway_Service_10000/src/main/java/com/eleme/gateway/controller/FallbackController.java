package com.eleme.gateway.controller;

import com.eleme.entity.common.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/user")
    public ApiResponse<String> userFallback() {
        return ApiResponse.fail(503, "用户服务暂时不可用，请稍后重试");
    }

    @RequestMapping("/merchant")
    public ApiResponse<String> merchantFallback() {
        return ApiResponse.fail(503, "商家服务暂时不可用，请稍后重试");
    }

    @RequestMapping("/cart")
    public ApiResponse<String> cartFallback() {
        return ApiResponse.fail(503, "购物车服务暂时不可用，请稍后重试");
    }

    @RequestMapping("/order")
    public ApiResponse<String> orderFallback() {
        return ApiResponse.fail(503, "订单服务暂时不可用，请稍后重试");
    }

    @RequestMapping("/payment")
    public ApiResponse<String> paymentFallback() {
        return ApiResponse.fail(503, "支付服务暂时不可用，请稍后重试");
    }
}