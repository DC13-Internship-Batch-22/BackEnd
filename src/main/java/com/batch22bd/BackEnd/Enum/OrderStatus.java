package com.batch22bd.BackEnd.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatus {
    PENDING(1,"PENDING"),
    CONFIRMED(2,"CONFIRMED");

    private Integer id;
    private String status;

}
