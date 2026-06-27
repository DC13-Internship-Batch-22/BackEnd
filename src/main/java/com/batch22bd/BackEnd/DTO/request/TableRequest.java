package com.batch22bd.BackEnd.DTO.request;

import com.batch22bd.BackEnd.Enum.TableStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record TableRequest(
        @NotBlank(message = "Table number is required")
        @Size(max = 5, message = "Table number must not exceed 10 characters")
        @Pattern(
                regexp = "^[A-Z][0-9]+$",
                message = "Format must be A001, T0012, B003"
        )
        @Schema(example = "A001")
        String tableNumber,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        @NotNull(message = "Status is required")
        TableStatus status
) {
}
