package com.eleme.entity.dto.order;

import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
        @NotNull Long userId,
        @NotNull Long merchantId,
        @NotNull Long addressId,
        String remark
) {
}
