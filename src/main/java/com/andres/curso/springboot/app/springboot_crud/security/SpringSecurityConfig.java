package com.andres.curso.springboot.app.springboot_crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
            ( authz ) -> authz
                .requestMatchers("/api/users") //? Endpoint publico para crear usuarios
                .permitAll() //? Cualquier usuario puede acceder a este endpoint
                .anyRequest().authenticated() //? Cualquier otra peticion, debe estar autenticada
        ).csrf( config -> config.disable() ) //? Deshabilitamos el CSRF, ya que no estamos usando sesiones
        .sessionManagement( management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS) ) //? Indicamos que no usaremos sesiones
        .build(); //? Construimos el filtro de seguridad
    }
}
