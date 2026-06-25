package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Entity.OrderItem;
import com.batch22bd.BackEnd.Entity.TableEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private long table_id;
    private List<OrderItem> items;
}
