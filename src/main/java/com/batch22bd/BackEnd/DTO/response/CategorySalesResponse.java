package com.batch22bd.BackEnd.DTO.response;

import java.math.BigDecimal;

public record CategorySalesResponse(
        Long categoryId,
        String categoryName,
        Long quantitySold,
        BigDecimal revenue
) {
}
