package com.eleme.entity.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddCartItemRequest(
        @NotNull Long merchantId,
        @NotNull Long foodId,
        @NotNull String foodName,
        @NotNull BigDecimal foodPrice,
        String foodImageUrl,
        @NotNull @Min(1) Integer quantity
) {
}
