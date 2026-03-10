package org.example.examen_5_6_ad_sergioarjona.repositories;

import org.example.examen_5_6_ad_sergioarjona.entities.UserDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de usuarios. Usado por AppUserDetailsService para autenticación.
 */
@Repository
public interface UserRepository extends MongoRepository<UserDB, String> {

    Optional<UserDB> findUserDBByEmail(String email);
}
