package com.batch22bd.BackEnd.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TableStatus {
    AVAILABLE(1,"AVAILABLE"),
    OCCUPIED(2,"OCCUPIED"),
    RESERVED(3,"RESERVED"),
    MAINTENANCE(4,"MAINTENANCE");

    private Integer id;
    private String status;
}
