package com.batch22bd.BackEnd.DTO.response;

import com.batch22bd.BackEnd.Entity.OrderItem;
import com.batch22bd.BackEnd.Enum.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RecentLargeTransactionResponse(
        Long orderId,
        String tableNumber,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItem> items
) {
}
