package com.batch22bd.BackEnd.DTO.response;

import com.batch22bd.BackEnd.Enum.FoodStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FoodResponse(
        Long id,
        String name,
        BigDecimal price,
        String imageUrl,
        FoodStatus status,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}