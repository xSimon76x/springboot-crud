package com.andres.curso.springboot.app.springboot_crud.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andres.curso.springboot.app.springboot_crud.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { 
    //? Clase Filtro para autenticar y generar el token JWT
    //? Se ejecuta cuando se hace una peticion a /login con el metodo POST

    private AuthenticationManager authenticationManager;

    //? Clave secreta para firmar el token
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

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
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        Map<String, String> body = new HashMap<>();
        
        //? Generamos el token, firmandolo con la clave secreta, y poniendole el username como subject
        String token = Jwts.builder().subject(username).signWith(SECRET_KEY).compact();

        //? Lo agregamos en la respuesta
        response.addHeader("Authorization", "Bearer " + token);

        body.put(token, token);
        body.put("username", username);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito!", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(200);
    }

    
    

}
