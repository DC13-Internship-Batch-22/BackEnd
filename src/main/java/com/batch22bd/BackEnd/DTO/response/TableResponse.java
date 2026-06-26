package com.batch22bd.BackEnd.DTO.response;

import com.batch22bd.BackEnd.Enum.TableStatus;

import java.time.LocalDateTime;

public record TableResponse(
        Long id,
        String tableNumber,
        Integer capacity,
        TableStatus status,
        Long orderId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
