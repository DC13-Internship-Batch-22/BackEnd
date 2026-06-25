package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Enum.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TableRequest(
        @NotBlank(message = "Table number is required")
        @Size(max = 10, message = "Table number must not exceed 10 characters")
        String tableNumber,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        @NotNull(message = "Status is required")
        TableStatus status
) {
}
