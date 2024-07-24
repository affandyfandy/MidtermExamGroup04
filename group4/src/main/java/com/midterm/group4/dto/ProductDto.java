package com.midterm.group4.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ProductDto {
    @NotBlank
    private String name;

    @Min(value = 0, message = "Quantity should not be less than 0")
    @NotNull(message = "Quantity can't be null")
    private Integer quantity;

    @Min(value = 0, message = "Stock should not be less than 0")
    @NotNull(message = "Stock can't be null")
    private Integer stock;

    private boolean isActive;

    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;

}
