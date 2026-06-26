package com.eleme.entity.dto.payment;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentCreateRequest(
        @NotNull Long orderId,
        @NotNull String orderNo,
        @NotNull BigDecimal amount,
        String payChannel
) {
}
