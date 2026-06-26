package com.eleme.order.controller;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.dto.order.OrderCreateRequest;
import com.eleme.entity.dto.order.PayStatusUpdateRequest;
import com.eleme.entity.vo.order.OrderConfirmVO;
import com.eleme.entity.vo.order.OrderVO;
import com.eleme.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/confirm")
    public ApiResponse<OrderConfirmVO> confirm(@RequestParam("userId") Long userId,
                                               @RequestParam("merchantId") Long merchantId) {
        return ApiResponse.ok(orderService.confirm(userId, merchantId));
    }

    @PostMapping
    public ApiResponse<OrderVO> create(@RequestBody @Valid OrderCreateRequest request) {
        OrderVO order = orderService.create(request);
        if (order == null) {
            return ApiResponse.fail(40010, "购物车为空");
        }
        return ApiResponse.ok(order);
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderVO> getOrder(@PathVariable("orderId") Long orderId) {
        return ApiResponse.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<OrderVO>> listUserOrders(@PathVariable("userId") Long userId) {
        return ApiResponse.ok(orderService.listUserOrders(userId));
    }

    @PutMapping("/{orderId}/pay-status")
    public ApiResponse<Void> updatePayStatus(@PathVariable("orderId") Long orderId,
                                             @RequestBody @Valid PayStatusUpdateRequest request) {
        if (!orderService.updatePayStatus(orderId, request.status())) {
            return ApiResponse.fail(404, "订单不存在");
        }
        return ApiResponse.ok();
    }
}
