package com.eleme.entity.feign;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.vo.cart.CartItemVO;
import com.eleme.entity.vo.cart.CartSummaryVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "eleme-cart-service", contextId = "cartFeignClient")
public interface CartFeignClient {
    @GetMapping("/api/carts/{userId}/merchants/{merchantId}")
    ApiResponse<List<CartItemVO>> getMerchantCartItems(@PathVariable("userId") Long userId,
                                                       @PathVariable("merchantId") Long merchantId);

    @GetMapping("/api/carts/{userId}/merchants/{merchantId}/summary")
    ApiResponse<CartSummaryVO> getMerchantCartSummary(@PathVariable("userId") Long userId,
                                                      @PathVariable("merchantId") Long merchantId);

    @DeleteMapping("/api/carts/{userId}/merchants/{merchantId}")
    ApiResponse<Void> clearMerchantCart(@PathVariable("userId") Long userId,
                                        @PathVariable("merchantId") Long merchantId);
}