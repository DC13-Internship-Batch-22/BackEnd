package com.batch22bd.BackEnd.DTO.response;

import java.math.BigDecimal;

public record ReportSummaryResponse(
        BigDecimal revenue,
        Long totalOrders,
        BigDecimal averageOrderValue,
        Long totalItemsSold,
        BigDecimal largestOrderAmount
) {
}
