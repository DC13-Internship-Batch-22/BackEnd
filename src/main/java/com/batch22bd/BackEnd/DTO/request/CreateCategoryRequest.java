package com.batch22bd.BackEnd.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest (
        @NotBlank(message = "Category name is required")
        @Size(min = 5, max = 100, message = "Category name must be between {min} and {max} characters")
        String name,

        @NotBlank(message = "Category description required")
        @Size(min = 10, max = 255, message = "Category description must be between {min} and {max} characters")
        String description
) {
}
