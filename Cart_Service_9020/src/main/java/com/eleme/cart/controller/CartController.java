package com.eleme.cart.controller;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.dto.cart.AddCartItemRequest;
import com.eleme.entity.vo.cart.CartItemVO;
import com.eleme.entity.vo.cart.CartSummaryVO;
import com.eleme.cart.service.CartService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}/merchants/{merchantId}")
    public ApiResponse<List<CartItemVO>> getMerchantCartItems(@PathVariable("userId") Long userId,
                                                              @PathVariable("merchantId") Long merchantId) {
        return ApiResponse.ok(cartService.getMerchantCartItems(userId, merchantId));
    }

    @GetMapping("/{userId}/merchants/{merchantId}/summary")
    public ApiResponse<CartSummaryVO> getMerchantCartSummary(@PathVariable("userId") Long userId,
                                                             @PathVariable("merchantId") Long merchantId) {
        return ApiResponse.ok(cartService.getMerchantCartSummary(userId, merchantId));
    }

    @PostMapping("/{userId}/items")
    public ApiResponse<Long> addItem(@PathVariable("userId") Long userId,
                                     @RequestBody @Valid AddCartItemRequest request) {
        return ApiResponse.ok(cartService.addItem(userId, request));
    }

    @PutMapping("/{userId}/items/{itemId}/increment")
    public ApiResponse<Void> increment(@PathVariable("userId") Long userId,
                                       @PathVariable("itemId") Long itemId) {
        cartService.increment(userId, itemId);
        return ApiResponse.ok();
    }

    @PutMapping("/{userId}/items/{itemId}/decrement")
    public ApiResponse<Void> decrement(@PathVariable("userId") Long userId,
                                       @PathVariable("itemId") Long itemId) {
        cartService.decrement(userId, itemId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{userId}/merchants/{merchantId}")
    public ApiResponse<Void> clearMerchantCart(@PathVariable("userId") Long userId,
                                               @PathVariable("merchantId") Long merchantId) {
        cartService.clearMerchantCart(userId, merchantId);
        return ApiResponse.ok();
    }
}
