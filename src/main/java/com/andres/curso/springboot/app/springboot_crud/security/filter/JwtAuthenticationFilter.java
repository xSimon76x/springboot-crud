package com.andres.curso.springboot.app.springboot_crud.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andres.curso.springboot.app.springboot_crud.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { 
    //? Clase Filtro para autenticar y generar el token JWT
    //? Se ejecuta cuando se hace una peticion a /login con el metodo POST

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        User user = null;
        String username = null;
        String password = null;

        //? Mapeamos el usuario que viene en el request, para obtener el username y password
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);

    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain chain,
        Authentication authResult
    ) throws IOException, ServletException {
        //? Para mas informacion de esto, en este repositorio: https://github.com/jwtk/jjwt
        // El cual viene de esta web de JWT: https://www.jwt.io/libraries?programming_language=java 

        Map<String, String> body = new HashMap<>();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal(); //? Obtenemos el usuario autenticado
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities(); //? Obtenemos los roles del usuario autenticado

        Claims claims = Jwts.claims()
            .add("authorities", new ObjectMapper().writeValueAsString(roles)) //? Agregamos los roles a los claims
            .add("username", username) //? Agregamos los roles a los claims
        .build(); //? Creamos los claims del token, que es la informacion que queremos guardar en el token

        //? Generamos el token, firmandolo con la clave secreta, y poniendole el username como subject
        String token = Jwts.builder()
            .subject(username)
            .claims(claims) //? Agregamos los claims al token
            .expiration(new Date(System.currentTimeMillis() + 3600000)) //? 1 hora de expiracion
            .issuedAt(new Date()) //? Fecha de creacion
            .signWith(SECRET_KEY)
            .compact();

        //? Lo agregamos en la respuesta
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        body.put("token", token);
        body.put("username", username);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito!", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException, ServletException {
        //? Si la autenticacion falla, se ejecuta este metodo
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error de autenticacion: username o password incorrectos!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
        
    }

    
    

}
