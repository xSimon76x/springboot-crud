package com.andres.curso.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andres.curso.springboot.app.springboot_crud.entities.User;
import com.andres.curso.springboot.app.springboot_crud.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    //* Para cargar el usuario mediante el username, mediante JPA */

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //? Buscar el usuario por username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema!", username));
        } 

        User user = userOptional.orElseThrow();

        //? Convertir la lista de roles en una lista de GrantedAuthority (que es lo que usa Spring Security)
        List<GrantedAuthority> authorities = user.getRols().stream()
            .map( role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        //? Retornar un UserDetails (que es lo que usa Spring Security)
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, //? accountNonExpired
            true, //? credentialsNonExpired
            true, //? accountNonLocked
            authorities
        );
    }

}
