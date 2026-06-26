package com.eleme.entity.vo.payment;

import com.eleme.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentVO(
        Long id,
        Long orderId,
        String orderNo,
        String payChannel,
        PaymentStatus status,
        String transactionNo,
        BigDecimal amount,
        OffsetDateTime createdAt,
        OffsetDateTime callbackAt
) {
}
