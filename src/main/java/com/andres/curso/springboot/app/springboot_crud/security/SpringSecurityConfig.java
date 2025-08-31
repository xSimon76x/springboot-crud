package com.andres.curso.springboot.app.springboot_crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.andres.curso.springboot.app.springboot_crud.security.filter.JwtAuthenticationFilter;
import com.andres.curso.springboot.app.springboot_crud.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //? Habilitamos la seguridad a nivel de metodo, para poder usar las anotaciones @PreAuthorize en los controladores
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        //? Configuramos el AuthenticationManager para que use el AuthenticationConfiguration
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        //? Configuramos el PasswordEncoder para que use BCrypt, que es un algoritmo de hash seguro
        // para codificar las contrase�as de los usuarios
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //? Configuramos las reglas de seguridad
        return http.authorizeHttpRequests(
            ( authz ) -> authz
            //Esta forma de aca se puede adaptar para que sea mas dinamico, con roles de la BD
            .requestMatchers(HttpMethod.GET, "/api/users").permitAll() //? Endpoint publico para crear usuarios, cualquier usuario puede acceder a este endpoint
            .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll() //? Endpoint publico para crear usuarios, cualquier usuario puede acceder a este endpoint
            // .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN") //? Endpoint protegido, solo los usuarios con rol ADMIN pueden acceder a este endpoint 
            // .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id}").hasAnyRole("ADMIN", "USER") //? Endpoint protegido, solo los usuarios con rol ADMIN pueden acceder a este endpoint 
            // .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN") //? Endpoint protegido, solo los usuarios con rol ADMIN pueden acceder a este endpoint 
            // .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("ADMIN") //? Endpoint protegido, solo los usuarios con rol ADMIN pueden acceder a este endpoint 
            // .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("ADMIN") //? Endpoint protegido, solo los usuarios con rol ADMIN pueden acceder a este endpoint 
            .anyRequest().authenticated() //? Cualquier otra peticion, debe estar autenticada
        )
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) //? Agregamos el filtro de autenticacion, que se ejecuta cuando se hace una peticion a /login
        .addFilter(new JwtValidationFilter(authenticationManager())) //? Agregamos el filtro de validacion, que se ejecuta en cada peticion para validar el token JWT
        .csrf( config -> config.disable() ) //? Deshabilitamos el CSRF, ya que no estamos usando sesiones
        .sessionManagement( management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS) ) //? Indicamos que no usaremos sesiones
        .build(); //? Construimos el filtro de seguridad
    }
}
