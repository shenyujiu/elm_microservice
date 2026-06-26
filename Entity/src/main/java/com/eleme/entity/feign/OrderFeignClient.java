package com.eleme.entity.feign;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.dto.order.PayStatusUpdateRequest;
import com.eleme.entity.vo.order.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "eleme-order-service", contextId = "orderFeignClient")
public interface OrderFeignClient {
    @PutMapping("/api/orders/{orderId}/pay-status")
    ApiResponse<Void> updatePayStatus(@PathVariable("orderId") Long orderId,
                                      @RequestBody PayStatusUpdateRequest request);

    @GetMapping("/api/orders/{orderId}")
    ApiResponse<OrderVO> getOrder(@PathVariable("orderId") Long orderId);
}