package com.andres.curso.springboot.app.springboot_crud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    //* Si uno define @NotEmpty por ejemplo, sin mensaje personalizado, considera el mensaje por defecto que tiene el app  */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotEmpty //? Validacion de campo no vacio. Para string no sirve el @NotNull
    @NotEmpty(message = "{NotEmpty.product.name}") //? Mensaje personalizado
    @Size(min = 3, max = 20) //? Validacion de longitud minima y maxima, solo para String
    private String name;

    //@Pattern//? Validacion para aplicar una expresion regular
    @Min(value = 1) //? Validacion de valor minimo
    @Max(value = 10000) //? Validacion de valor maximo
    // @NotNull //? Validacion de campo no nulo, para los demas tipos, a excepcion de String
    @NotNull(message = "{NotNull.product.price}") //? Mensaje personalizado
    private Integer price;

    // @NotBlank //? Es mejor que un NotEmpty, ya que valida que no sea un espacio en blanco, aparte de que no sea vacio
    @NotBlank(message = "{NotBlank.product.description}") //? Mensaje personalizado
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

}
