package com.andres.curso.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.curso.springboot.app.springboot_crud.entities.Product;
import com.andres.curso.springboot.app.springboot_crud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductServices{

    @Autowired
    private ProductRepository repository; 

    @Override
    @Transactional( readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    @Override
    @Transactional( readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> delete(Product product) {

        Optional<Product> productOptional = repository.findById(product.getId());

        productOptional.ifPresent( p -> {
            repository.delete(product);
        });

        return productOptional;
    }

}
