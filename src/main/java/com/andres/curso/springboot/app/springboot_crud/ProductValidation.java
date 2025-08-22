package com.andres.curso.springboot.app.springboot_crud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.andres.curso.springboot.app.springboot_crud.entities.Product;

@Component
public class ProductValidation implements Validator {

    @Value("${NotEmpty.product.name}")
    private String errorDescription;

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(
            errors, // Siempre se pasa error primero
            "name", // Se pasa el nombre del campo del objeto que se va a validar
            null,
            "NotEmpty.product.name" // (va sin llaves) El mensaje de error que se va a mostrar, en este caso es lo definido en el messages.properties
        );

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", null, "{NotBlank.product.description}"); Asi no toma el mensaje del messages.properties
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", null, errorDescription); //? de esta forma si toma el mensaje del messages.properties
        }

        if (product.getPrice() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", null, "NotNull.product.price");
        } else if (product.getPrice() <= 0) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", null, "Min.product.price");
        }

        // Aquí puedes agregar más validaciones personalizadas si es necesario
    }

}
