package com.andres.curso.springboot.app.springboot_crud.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //? Valida que el valor no sea nulo, no esté vacío y no sea solo espacios en blanco
        // return (value != null && !value.isEmpty() && !value.trim().isEmpty()); 

        return StringUtils.hasText(value); //? Usando el método de Spring que hace lo mismo que la validación comentada arriba
    }



}
