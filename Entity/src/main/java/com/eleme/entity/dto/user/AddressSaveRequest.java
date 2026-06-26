package com.eleme.entity.dto.user;

import jakarta.validation.constraints.NotBlank;

public record AddressSaveRequest(
        @NotBlank String contactName,
        @NotBlank String contactPhone,
        String province,
        String city,
        String district,
        @NotBlank String detail,
        String tag,
        Boolean isDefault
) {
}
