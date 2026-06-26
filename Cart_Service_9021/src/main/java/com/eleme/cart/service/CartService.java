package com.eleme.cart.service;

import com.eleme.cart.mapper.CartItemMapper;
import com.eleme.cart.model.CartItemDO;
import com.eleme.entity.dto.cart.AddCartItemRequest;
import com.eleme.entity.vo.cart.CartItemVO;
import com.eleme.entity.vo.cart.CartSummaryVO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private final CartItemMapper cartItemMapper;

    public CartService(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public List<CartItemVO> getMerchantCartItems(Long userId, Long merchantId) {
        return cartItemMapper.selectByMerchant(userId, merchantId).stream()
                .map(this::toVO)
                .toList();
    }

    public CartSummaryVO getMerchantCartSummary(Long userId, Long merchantId) {
        List<CartItemDO> items = cartItemMapper.selectByMerchant(userId, merchantId);
        BigDecimal goodsAmount = items.stream()
                .map(i -> i.getFoodPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = items.stream().mapToInt(CartItemDO::getQuantity).sum();
        return new CartSummaryVO(userId, merchantId, goodsAmount, totalQuantity);
    }

    @Transactional
    public Long addItem(Long userId, AddCartItemRequest request) {
        CartItemDO existing = cartItemMapper.selectByUniqueKey(userId, request.merchantId(), request.foodId());
        if (existing != null) {
            existing.setFoodName(request.foodName());
            existing.setFoodPrice(request.foodPrice());
            existing.setFoodImageUrl(request.foodImageUrl());
            existing.setQuantity(existing.getQuantity() + request.quantity());
            existing.setChecked(true);
            cartItemMapper.update(existing);
            return existing.getId();
        }

        CartItemDO item = new CartItemDO();
        item.setUserId(userId);
        item.setMerchantId(request.merchantId());
        item.setFoodId(request.foodId());
        item.setFoodName(request.foodName());
        item.setFoodPrice(request.foodPrice());
        item.setFoodImageUrl(request.foodImageUrl());
        item.setQuantity(request.quantity());
        item.setChecked(true);
        cartItemMapper.insert(item);

        CartItemDO saved = cartItemMapper.selectByUniqueKey(userId, request.merchantId(), request.foodId());
        return saved == null ? null : saved.getId();
    }

    @Transactional
    public void increment(Long userId, Long itemId) {
        updateQuantity(userId, itemId, 1);
    }

    @Transactional
    public void decrement(Long userId, Long itemId) {
        updateQuantity(userId, itemId, -1);
    }

    @Transactional
    public void clearMerchantCart(Long userId, Long merchantId) {
        cartItemMapper.deleteByMerchant(userId, merchantId);
    }

    private void updateQuantity(Long userId, Long itemId, int delta) {
        CartItemDO item = cartItemMapper.selectByIdAndUserId(itemId, userId);
        if (item == null) {
            return;
        }
        int newQuantity = item.getQuantity() + delta;
        if (newQuantity <= 0) {
            cartItemMapper.deleteById(itemId, userId);
            return;
        }
        item.setQuantity(newQuantity);
        cartItemMapper.update(item);
    }

    private CartItemVO toVO(CartItemDO item) {
        return new CartItemVO(
                item.getId(),
                item.getUserId(),
                item.getMerchantId(),
                item.getFoodId(),
                item.getFoodName(),
                item.getFoodPrice(),
                item.getFoodImageUrl(),
                item.getQuantity(),
                item.getChecked()
        );
    }
}
