package org.example.examen_5_6_ad_sergioarjona.security;

import org.example.examen_5_6_ad_sergioarjona.entities.UserDB;
import org.example.examen_5_6_ad_sergioarjona.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Carga los usuarios desde MongoDB para la autenticación.
 * El rol (ADMIN/USER) se obtiene del campo "role"
 */
@Service
class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga el usuario por email (usado como username en Basic Auth).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDB> currentUser = userRepository.findUserDBByEmail(username);

        if (currentUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        UserDB user = currentUser.get();
        String role = (user.getRole() != null && !user.getRole().isBlank())
                ? user.getRole().toUpperCase()
                : "USER";

        return User.withUsername(username)
                .password("{noop}" + user.getPassword())
                .roles(role)
                .build();
    }
}
