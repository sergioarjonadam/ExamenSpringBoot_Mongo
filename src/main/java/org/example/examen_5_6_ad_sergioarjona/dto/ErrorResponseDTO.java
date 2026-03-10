package org.example.examen_5_6_ad_sergioarjona.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO para respuestas de error en JSON (400, 404, 401).
 */
@AllArgsConstructor
@Getter
public class ErrorResponseDTO {
    private String error;
    private String message;
    private Integer errorCode;
}
