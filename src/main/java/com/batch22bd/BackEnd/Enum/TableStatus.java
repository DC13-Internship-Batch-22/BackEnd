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
    RESERVED(1,"RESERVED"),
    MAINTENANCE(1,"MAINTENANCE");

    private Integer id;
    private String status;
}
