package com.batch22bd.BackEnd.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatus {
    PENDING(1,"Pending"),
    CONFIRMED(2,"Confirmed");

    private Integer id;
    private String status;

}
