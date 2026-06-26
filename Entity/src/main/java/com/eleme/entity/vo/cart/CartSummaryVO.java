package com.eleme.entity.vo.cart;

import java.math.BigDecimal;

public record CartSummaryVO(
        Long userId,
        Long merchantId,
        BigDecimal goodsAmount,
        Integer totalQuantity
) {
}
