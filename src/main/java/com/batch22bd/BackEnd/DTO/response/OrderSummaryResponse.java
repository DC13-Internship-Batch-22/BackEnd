package com.batch22bd.BackEnd.DTO.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSummaryResponse {
    private Long order_id;
    private String tableNumber;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private String orderStatus;
}
