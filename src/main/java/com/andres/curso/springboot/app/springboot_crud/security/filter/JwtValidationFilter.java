package com.andres.curso.springboot.app.springboot_crud.security.filter;

import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {
    //* Filtro para validar el token JWT en cada peticion */

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        //* Llamamos al constructor de la clase padre */
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain chain
    ) throws IOException, ServletException {
        //* Metodo que se ejecuta en cada peticion, para validar el token JWT */
        
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            //? Validamos el token, si es valido, obtenemos los claims
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    

}
