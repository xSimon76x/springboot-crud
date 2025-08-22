package com.andres.curso.springboot.app.springboot_crud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.andres.curso.springboot.app.springboot_crud.entities.Users;

public interface UserRepository extends CrudRepository<Users, Long> {

}
