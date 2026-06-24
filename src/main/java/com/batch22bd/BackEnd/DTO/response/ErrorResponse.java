package com.batch22bd.BackEnd.DTO.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        String path,
        Map<String, String> errors,
        Instant timestamp
) {
    public ErrorResponse(String message, String path, Instant timestamp) {
        this(message, path, null, timestamp);
    }
}
