package org.example.examen_5_6_ad_sergioarjona.exceptions;

/**
 * Se lanza cuando no existe un ítem con el id indicado (Historias B, C).
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
