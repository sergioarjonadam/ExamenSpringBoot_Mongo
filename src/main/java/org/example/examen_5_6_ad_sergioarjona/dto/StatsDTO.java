package org.example.examen_5_6_ad_sergioarjona.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * DTO de respuesta para la Historia F - Estadísticas de la tienda.
 * a) totalItems, b) itemsWithLowStock, c) manufacturers
 */
@AllArgsConstructor
@Getter
public class StatsDTO {
    private long totalItems;           // F.a - Número total de ítems
    private long itemsWithLowStock;    // F.b - Ítems con stock < 100
    private List<String> manufacturers; // F.c - Listado de fabricantes
}
