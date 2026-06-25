package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Enum.TopDishSortBy;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopSellingDishesRequest extends DateRangeRequest {

    @Min(value = 1, message = "limit must be at least 1")
    @Max(value = 100, message = "limit must be less than or equal to 100")
    private Integer limit = 10;

    private TopDishSortBy sortBy = TopDishSortBy.QUANTITY;
}
