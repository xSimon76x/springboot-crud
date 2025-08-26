package com.andres.curso.springboot.app.springboot_crud.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {
    //* Clase abstracta para que Jackson pueda deserializar el objeto SimpleGrantedAuthority */


    public SimpleGrantedAuthorityJsonCreator( @JsonProperty("authority") String role) {
        //? Constructor necesario para que Jackson pueda deserializar el objeto
    }   

}
