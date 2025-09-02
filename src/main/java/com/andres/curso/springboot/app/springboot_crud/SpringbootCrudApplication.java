package com.andres.curso.springboot.app.springboot_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringbootCrudApplication 
	extends SpringBootServletInitializer //? Para que funcione el .war en un servidor externo como Tomcat v.10
{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootCrudApplication.class, args);
	}

}
