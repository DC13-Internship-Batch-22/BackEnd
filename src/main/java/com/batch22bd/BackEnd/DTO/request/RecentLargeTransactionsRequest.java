package com.batch22bd.BackEnd.DTO.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RecentLargeTransactionsRequest extends DateRangeRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "minAmount must be greater than or equal to 0")
    private BigDecimal minAmount = BigDecimal.valueOf(200000);

    @Min(value = 1, message = "limit must be at least 1")
    @Max(value = 100, message = "limit must be less than or equal to 100")
    private Integer limit = 10;
}
