package org.example.examen_5_6_ad_sergioarjona.controllers;

import org.example.examen_5_6_ad_sergioarjona.dto.ErrorResponseDTO;
import org.example.examen_5_6_ad_sergioarjona.exceptions.InvalidRequestException;
import org.example.examen_5_6_ad_sergioarjona.exceptions.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestiona las excepciones de forma global y devuelve respuestas de error estructuradas en JSON.
 * Usado por las historias B, C y E cuando no se encuentra un ítem o la petición es inválida.
 */
@RestControllerAdvice
class ItemControllerAdvice {

    /**
     * Captura ItemNotFoundException (Historia B, C).
     * Devuelve 404 cuando se intenta obtener o eliminar un ítem que no existe.
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleItemNotFound(ItemNotFoundException ex) {
        ErrorResponseDTO err = new ErrorResponseDTO("Ítem no encontrado", ex.getMessage(), 404);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(err);
    }

    /**
     * Captura InvalidRequestException (Historias A, E).
     * Devuelve 400 cuando los datos de la petición son incorrectos.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidRequest(InvalidRequestException ex) {
        ErrorResponseDTO err = new ErrorResponseDTO("Petición inválida", ex.getMessage(), 400);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(err);
    }
}
