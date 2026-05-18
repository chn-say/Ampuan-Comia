package com.ws101.Ampuan.Comia.EcommerceApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 4, message = "Password must be at least 4 characters long")
        String password,

        @NotBlank(message = "Role cannot be empty")
        String role
) {}