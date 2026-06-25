package com.batch22bd.BackEnd.DTO.response;

import com.batch22bd.BackEnd.Enum.OrderStatus;

public record OrderStatusReportResponse(
        OrderStatus status,
        Long count
) {
}
