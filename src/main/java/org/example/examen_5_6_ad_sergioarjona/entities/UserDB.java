package org.example.examen_5_6_ad_sergioarjona.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Usuario para autenticación. ADMIN puede realizar Historias A, B, E.
 */
@Data
@Document(collection = "users")
public class UserDB {
    @Id
    private String _id;
    private String email;     // Usado como username en Basic Auth
    private String password;
    private String role;      // "ADMIN" o "USER"
}
