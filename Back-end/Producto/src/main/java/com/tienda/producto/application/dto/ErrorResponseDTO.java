package com.tienda.producto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para respuestas de error.
 * 
 * @author Tienda Italo Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private String message;
    private String error;
    private int statusCode;
    private String path;
    private String method;
    private LocalDateTime timestamp;
    private List<String> validationErrors;
    private Map<String, String> fieldErrors;
    private String traceId;
    private String requestId;

    public static ErrorResponseDTO validationError(String message, List<String> validationErrors) {
        return ErrorResponseDTO.builder()
                .message(message)
                .validationErrors(validationErrors)
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponseDTO fieldError(String message, Map<String, String> fieldErrors) {
        return ErrorResponseDTO.builder()
                .message(message)
                .fieldErrors(fieldErrors)
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponseDTO notFound(String message) {
        return ErrorResponseDTO.builder()
                .message(message)
                .statusCode(404)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponseDTO internalError(String message) {
        return ErrorResponseDTO.builder()
                .message(message)
                .statusCode(500)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
