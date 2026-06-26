package com.eleme.entity.vo.cart;

import java.math.BigDecimal;

public record CartItemVO(
        Long id,
        Long userId,
        Long merchantId,
        Long foodId,
        String foodName,
        BigDecimal foodPrice,
        String foodImageUrl,
        Integer quantity,
        Boolean checked
) {
}
