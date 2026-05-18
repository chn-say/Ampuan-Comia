package com.ws101.Ampuan.Comia.EcommerceApi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductDto(
        @NotBlank(message = "Product name cannot be empty")
        String prodName,

        @NotNull(message = "Product price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        Double prodPrice, // Binago mula double -> Double para tanggapin ang @NotNull

        String prodDescription
) {}