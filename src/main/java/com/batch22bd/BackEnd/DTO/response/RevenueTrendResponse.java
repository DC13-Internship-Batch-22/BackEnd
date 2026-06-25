package com.batch22bd.BackEnd.DTO.response;

import java.math.BigDecimal;

public record RevenueTrendResponse(
        String period,
        BigDecimal revenue,
        Long orderCount
) {
}
