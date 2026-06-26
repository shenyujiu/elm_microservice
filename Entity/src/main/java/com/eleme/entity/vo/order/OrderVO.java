package com.eleme.entity.vo.order;

import com.eleme.entity.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderVO(
        Long id,
        String orderNo,
        Long userId,
        Long merchantId,
        String merchantName,
        BigDecimal goodsAmount,
        BigDecimal deliveryFee,
        BigDecimal payAmount,
        OrderStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime paidAt,
        List<OrderItemVO> items
) {
}
