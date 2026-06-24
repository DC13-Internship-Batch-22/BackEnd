package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Enum.FoodStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateFoodRequest(
        @Size(min = 2, max = 150, message = "Food name must be between {min} and {max} characters")
        String name,

        @DecimalMin(value = "0.0", inclusive = false, message = "Food price must be greater than 0")
        BigDecimal price,

        Long categoryId,

        @Size(max = 500, message = "Image URL must not exceed {max} characters")
        String imageUrl,

        FoodStatus status
) {
}