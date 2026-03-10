package org.example.examen_5_6_ad_sergioarjona.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un artículo de la tienda.
 * Usada en todas las historias de usuario (A-F).
 */
@Data
@Document(collection = "items")
public class Item {
    @Id
    private String _id;           // Identificador MongoDB (generado automáticamente)
    @Indexed(unique = true)
    private Integer id;           // Identificador único interno para la aplicación
    private String title;         // Nombre del artículo
    private Double price;         // Precio
    private String category;      // Categoría (Historia D, E)
    private String description;   // Descripción (puede incluir HTML)
    private Double rate;          // Valoración media (1-10)
    private Integer count;        // Cantidad de veces vendido
    private String manufacturer;  // Fabricante (Historia F.c)
    private String color;         // Color del producto
    private String EAN;           // Código de barras
    private String image;         // URL de la imagen
    private Integer stock;        // Unidades en almacén (Historia F.b - stock bajo)
}
