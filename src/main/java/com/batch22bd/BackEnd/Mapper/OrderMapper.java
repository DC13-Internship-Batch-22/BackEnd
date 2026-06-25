package com.batch22bd.BackEnd.Mapper;

import com.batch22bd.BackEnd.DTO.response.OrderDetailResponse;
import com.batch22bd.BackEnd.DTO.response.OrderSummaryResponse;
import com.batch22bd.BackEnd.Entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderSummaryResponse toSummaryResponse(Order order) {
        return OrderSummaryResponse.builder()
                    .order_id(order.getId())
                    .tableNumber(order.getTable().getTableNumber())
                    .createdAt(order.getCreatedAt())
                    .totalAmount(order.getTotalAmount())
                    .orderStatus(order.getStatus().toString())
                    .build();
    }

    public OrderDetailResponse toDetailResponse(Order order) {
        return OrderDetailResponse.builder()
                    .order_id(order.getId())
                    .tableNumber(order.getTable().getTableNumber())
                    .items(order.getItems())
                    .createdAt(order.getCreatedAt())
                    .totalAmount(order.getTotalAmount())
                    .orderStatus(order.getStatus().toString())
                    .build();
    }
}
