package com.andres.curso.springboot.app.springboot_crud.security.filter;

import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.CONTENT_TYPE;
import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.andres.curso.springboot.app.springboot_crud.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.andres.curso.springboot.app.springboot_crud.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
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
            //? Si el header es nulo o no empieza con Bearer, continuamos con la cadena de filtros
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            //? Validamos el token, si es valido, obtenemos los claims
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String username = claims.getSubject();
            // String username2 = (String) claims.get("username"); //? Otra forma de obtener algun dato de los claims
            Object authoritiesClaims = claims.get("authorities");
            
            //? Creamos un objeto Authentication con los datos del usuario y los roles
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
            );
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, //? nombre del usuario
                null, //? password (no lo necesitamos) solo se usa en el login, en el attemptAuthentication
                authorities //? roles
            );
            
            SecurityContextHolder.getContext().setAuthentication(authToken); //? Seteamos el usuario autenticado en el contexto de seguridad
            chain.doFilter(request, response); //? Continuamos con la cadena de filtros
        } catch (Exception e) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("mensaje", "Token no valido");
            
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
    }

    

}
