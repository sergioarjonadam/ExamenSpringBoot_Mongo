package org.example.examen_5_6_ad_sergioarjona.repositories;

import org.example.examen_5_6_ad_sergioarjona.entities.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos de ítems.
 * findItemById: Historias A, B, C
 * findItemsByCategory: Historia D
 * count/findAll: Historia F
 */
@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    Optional<Item> findItemById(Integer id);
    List<Item> findItemsByCategory(String category);
}
