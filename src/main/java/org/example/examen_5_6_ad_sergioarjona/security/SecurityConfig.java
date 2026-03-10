package org.example.examen_5_6_ad_sergioarjona.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.examen_5_6_ad_sergioarjona.dto.ErrorResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security.
 * Protege únicamente las operaciones de escritura con autenticación básica.
 * Historias A, B, E requieren rol ADMIN.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura las reglas de autorización:
     * - GET: permitido a todos (Historias C, D, F)
     * - POST /api/items: solo ADMIN (Historia A)
     * - DELETE /api/items/*: solo ADMIN (Historia B)
     * - PUT /api/items/category: solo ADMIN (Historia E)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/items").hasRole("ADMIN")        // Historia A
                        .requestMatchers(HttpMethod.DELETE, "/api/items/**").hasRole("ADMIN")   // Historia B
                        .requestMatchers(HttpMethod.PUT, "/api/items/category").hasRole("ADMIN") // Historia E
                        .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
                        .anyRequest().permitAll()
                ).httpBasic(basic -> basic.authenticationEntryPoint(customAuthenticationEntryPoint()));
        return http.build();
    }

    /**
     * Punto de entrada cuando la autenticación falla. Devuelve JSON con error 401.
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, e) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Usuario no autorizado",
                    "El usuario y contraseña no coinciden",
                    401
            );
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(error));
        };
    }
}
