package org.example.examen_5_6_ad_sergioarjona.services;

import org.example.examen_5_6_ad_sergioarjona.dto.StatsDTO;
import org.example.examen_5_6_ad_sergioarjona.entities.Item;
import org.example.examen_5_6_ad_sergioarjona.exceptions.InvalidRequestException;
import org.example.examen_5_6_ad_sergioarjona.exceptions.ItemNotFoundException;
import org.example.examen_5_6_ad_sergioarjona.repositories.ItemRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio que implementa la lógica de las historias de usuario.
 */
@Service
public class ItemService {

    /** Umbral para considerar stock bajo (Historia F.b). Por defecto 100. */
    public static final int LOW_STOCK_THRESHOLD = 100;

    private final ItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;

    public ItemService(ItemRepository itemRepository, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * HISTORIA A - Añade un nuevo ítem a la tienda.
     */
    public Item addItem(Item item) {
        if (item.getId() == null) {
            throw new InvalidRequestException("El campo 'id' es obligatorio");
        }
        if (itemRepository.findItemById(item.getId()).isPresent()) {
            throw new InvalidRequestException("Ya existe un ítem con id " + item.getId());
        }
        return itemRepository.save(item);
    }

    /**
     * HISTORIA B - Elimina un ítem de la tienda por su id interno.
     */
    public void deleteItem(Integer id) {
        Item item = itemRepository.findItemById(id)
                .orElseThrow(() -> new ItemNotFoundException("Ítem con id " + id + " no encontrado"));
        itemRepository.delete(item);
    }

    /**
     * HISTORIA C - Obtiene los detalles de un ítem por su identificador.
     */
    public Item getItemById(Integer id) {
        return itemRepository.findItemById(id)
                .orElseThrow(() -> new ItemNotFoundException("Ítem con id " + id + " no encontrado"));
    }

    /**
     * HISTORIA D - Lista todos los ítems de una categoría específica.
     */
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findItemsByCategory(category);
    }

    /**
     * HISTORIA E - Cambia la categoría de todos los ítems que tengan la categoría antigua por la nueva.
     */
    public void changeCategory(String oldCategory, String newCategory) {
        if (oldCategory == null || oldCategory.isBlank()) {
            throw new InvalidRequestException("La categoría antigua no puede estar vacía");
        }
        if (newCategory == null || newCategory.isBlank()) {
            throw new InvalidRequestException("La nueva categoría no puede estar vacía");
        }
        mongoTemplate.updateMulti(
                new Query(Criteria.where("category").is(oldCategory)),
                new Update().set("category", newCategory),
                Item.class
        );
    }

    /**
     * HISTORIA F - Genera las estadísticas de la tienda:
     * a) Total de ítems
     * b) Ítems con stock inferior al umbral (100)
     * c) Listado de fabricantes
     */
    public StatsDTO getStats() {
        long total = itemRepository.count();
        long lowStock = itemRepository.findAll().stream()
                .filter(item -> (item.getStock() != null && item.getStock() < LOW_STOCK_THRESHOLD))
                .count();
        List<String> manufacturers = itemRepository.findAll().stream()
                .map(Item::getManufacturer)
                .filter(m -> m != null && !m.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return new StatsDTO(total, lowStock, manufacturers);
    }
}
