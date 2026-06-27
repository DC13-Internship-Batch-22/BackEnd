package com.batch22bd.BackEnd.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class LoginRequest {
    @NotBlank(message = "Username required")
    @Schema(example = "admin")
    private String username;
    @NotBlank(message = "Password required")
    @Size(min = 6, max = 10, message = "message invalid")
    @Schema(example = "admin123")
    private String password;
}
