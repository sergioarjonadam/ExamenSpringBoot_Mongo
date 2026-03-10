package org.example.examen_5_6_ad_sergioarjona.exceptions;

/**
 * Se lanza cuando la petición tiene datos inválidos (Historias A, E).
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
