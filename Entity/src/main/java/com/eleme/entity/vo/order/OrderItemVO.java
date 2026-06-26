package com.eleme.entity.vo.order;

import java.math.BigDecimal;

public record OrderItemVO(
        Long id,
        Long foodId,
        String foodName,
        BigDecimal foodPrice,
        Integer quantity,
        BigDecimal amount
) {
}
