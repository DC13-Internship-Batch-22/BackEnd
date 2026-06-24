package com.batch22bd.BackEnd.DTO.request;

import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest (
        @Size(min = 5, max = 100, message = "Category name must be between {min} and {max} characters")
        String name,

        @Size(min = 10, max = 255, message = "Category description must be between {min} and {max} characters")
        String description
) {
}
