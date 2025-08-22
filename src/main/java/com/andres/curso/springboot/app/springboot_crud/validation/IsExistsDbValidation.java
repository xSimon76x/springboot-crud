package com.andres.curso.springboot.app.springboot_crud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andres.curso.springboot.app.springboot_crud.services.ProductServices;

import jakarta.validation.ConstraintValidator;

@Component
public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, String> {

    @Autowired
    private ProductServices productServices;

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return !productServices.existsBySku(value);
    }

}
