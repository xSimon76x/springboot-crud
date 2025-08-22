package com.andres.curso.springboot.app.springboot_crud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.andres.curso.springboot.app.springboot_crud.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
