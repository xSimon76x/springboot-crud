package com.andres.curso.springboot.app.springboot_crud.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //? Valida que el valor no sea nulo, no est� vac�o y no sea solo espacios en blanco
        // return (value != null && !value.isEmpty() && !value.trim().isEmpty()); 

        return StringUtils.hasText(value); //? Usando el m�todo de Spring que hace lo mismo que la validaci�n comentada arriba
    }



}
