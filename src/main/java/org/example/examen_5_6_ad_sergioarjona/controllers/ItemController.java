package org.example.examen_5_6_ad_sergioarjona.controllers;

import org.example.examen_5_6_ad_sergioarjona.dto.StatsDTO;
import org.example.examen_5_6_ad_sergioarjona.entities.Item;
import org.example.examen_5_6_ad_sergioarjona.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST que expone los endpoints de la API de la tienda.
 * Cada método está asociado a una historia de usuario concreta.
 */
@RestController
@RequestMapping("/api")
class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * HISTORIA A - Agregar un nuevo ítem
     * Como usuario, quiero poder agregar un nuevo ítem proporcionando sus detalles para almacenarlo en la tienda.
     * Requiere rol ADMIN.
     */
    @PostMapping("/items")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item saved = itemService.addItem(item);
        return ResponseEntity.ok(saved);
    }

    /**
     * HISTORIA B - Eliminar un ítem
     * Como usuario, quiero poder eliminar un ítem concreto de la tienda.
     * Requiere rol ADMIN.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * HISTORIA C - Obtener los detalles de un ítem
     * Como usuario, quiero poder obtener la información detallada de un ítem específico proporcionando su identificador.
     */
    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    /**
     * HISTORIA D - Listar ítems por categoría
     * Como usuario, quiero poder obtener una lista de todos los ítems de una categoría específica
     * para explorar opciones dentro de un grupo concreto.
     */
    @GetMapping(value = "/items", params = "category")
    public List<Item> getItemsByCategory(@RequestParam String category) {
        return itemService.getItemsByCategory(category);
    }

    /**
     * HISTORIA E - Cambiar categoría
     * Como administrador, quiero poder cambiar el texto de categoría a uno nuevo a todos los ítems
     * que pertenezcan a dicha categoría. Requiere rol ADMIN.
     */
    @PutMapping("/items/category")
    public ResponseEntity<Void> changeCategory(@RequestBody Map<String, String> body) {
        String oldCategory = body.get("oldCategory");
        String newCategory = body.get("newCategory");
        itemService.changeCategory(oldCategory, newCategory);
        return ResponseEntity.ok().build();
    }

    /**
     * HISTORIA F - Mostrar estadísticas de la tienda
     * Como usuario, quiero obtener un resumen estadístico de la tienda que incluya:
     * a) Número total de ítems almacenados
     * b) Ítems con stock inferior a una constante (100 por defecto)
     * c) Listado de fabricantes (solo el nombre)
     */
    @GetMapping("/stats")
    public StatsDTO getStats() {
        return itemService.getStats();
    }
}
