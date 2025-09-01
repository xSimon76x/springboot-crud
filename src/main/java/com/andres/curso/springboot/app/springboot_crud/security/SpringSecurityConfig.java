package com.andres.curso.springboot.app.springboot_crud.security;

import java.nio.file.DirectoryStream.Filter;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfigurationSource;

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
        .cors(cors -> cors.configurationSource(corsConfigurationSource()) ) //? Habilitamos el CORS, para permitir peticiones desde otros dominios (front-end) CORS Parte 3
        .sessionManagement( management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS) ) //? Indicamos que no usaremos sesiones
        .build(); //? Construimos el filtro de seguridad
    }

    //CORS Parte 2
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        //? Configuramos el CORS, para permitir peticiones desde otros dominios (front-end)
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins( Arrays.asList("*") ); //? Permitimos peticiones desde cualquier origen, en produccion se debe especificar el dominio del front-end
        config.setAllowedMethods( Arrays.asList("GET", "POST", "PUT", "DELETE") ); //? Permitimos los metodos HTTP que usara el front-end
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); //? Permitimos los headers que usara el front-end
        config.setAllowCredentials(true); //? Permitimos el uso de cookies, si es necesario

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    //CORS Parte 2
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        //? Registramos el filtro de CORS

        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
            new CorsFilter(corsConfigurationSource())
        ); //? Registramos el filtro de CORS
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE); //? Establecemos el orden del filtro, con el orden mas alto para que se ejecute primero
        return corsBean;
    }
}
