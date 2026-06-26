package com.eleme.entity.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        String phone,
        String nickname,
        String gender
) {
}
