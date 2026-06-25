package com.batch22bd.BackEnd.DTO.response;

import java.math.BigDecimal;

public record TopSellingDishResponse(
        Long productId,
        String productName,
        Long quantitySold,
        BigDecimal revenue
) {
}
