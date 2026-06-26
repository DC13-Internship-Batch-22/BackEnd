package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.DTO.OrderDto;
import lombok.Data;

import java.util.List;

@Data
public class AssignOrderRequest {
    private long table_id;
    private List<OrderDto> items;
}
