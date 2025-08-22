package com.andres.curso.springboot.app.springboot_crud;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties") //? con esto podemos cargar un archivo de propiedades personalizado
public class AppConfig {
    //* Queda mas limpio tener esta clase, para hacer estas definiciones de properties */

}
