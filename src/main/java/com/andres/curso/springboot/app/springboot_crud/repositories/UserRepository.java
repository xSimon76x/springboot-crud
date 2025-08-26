package com.andres.curso.springboot.app.springboot_crud.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.andres.curso.springboot.app.springboot_crud.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.rols WHERE u.username = ?1")
    Optional<User> findUserByUsernameWithRoles(String username);
}
