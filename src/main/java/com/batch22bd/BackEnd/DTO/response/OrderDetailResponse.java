package com.batch22bd.BackEnd.DTO.response;

import com.batch22bd.BackEnd.Entity.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {
    private Long order_id;
    private String tableNumber;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private String orderStatus;
}
