package com.andres.curso.springboot.app.springboot_crud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = IsExistsDbValidation.class) //? Indica que esta anotación usa la clase IsExistsDbValidation para validar
@Target(ElementType.FIELD) //? Se puede aplicar a campos
@Retention(RetentionPolicy.RUNTIME) //? Retención en tiempo de ejecución
public @interface IsExistsDb {

    String message() default "ya existe en la base de datos!"; //? Mensaje por defecto si no se define uno personalizado

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
