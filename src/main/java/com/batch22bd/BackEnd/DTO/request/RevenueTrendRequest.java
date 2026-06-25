package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Enum.RevenueGroupBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueTrendRequest extends DateRangeRequest {

    private RevenueGroupBy groupBy = RevenueGroupBy.DAY;
}
