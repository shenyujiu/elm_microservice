package com.eleme.entity.dto.order;

import com.eleme.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record PayStatusUpdateRequest(
        @NotNull OrderStatus status
) {
}
