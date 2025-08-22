package com.andres.curso.springboot.app.springboot_crud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RequiredValidation.class) //? Indica que esta anotación usa la clase RequiredValidation para validar
@Retention(RetentionPolicy.RUNTIME) //? Retención en tiempo de ejecución
@Target({ElementType.FIELD, ElementType.METHOD}) //? Se puede aplicar a campos y métodos, de clases
public @interface IsRequired { //? interface con el arroba, es una anotación personalizada

    String message() default "es requerido usando anotaciones"; //? Mensaje por defecto si no se define uno personalizado

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
