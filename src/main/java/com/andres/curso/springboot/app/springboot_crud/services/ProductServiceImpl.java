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
    public Optional<Product> update(Long id, Product product) {
        
        Optional<Product> productOptional = repository.findById(id);

        if (productOptional.isPresent()) {
            Product productDB = productOptional.get(); // o tambien puede ser productOptional.orElseThrow();
            productDB.setName(product.getName()); // y asi con los demas campos que se quieran actualizar
            productDB.setDescription(product.getDescription());
            productDB.setPrice(product.getPrice()); 
            return Optional.of(repository.save(productDB));
        }

        return productOptional;
    }    

    @Override
    @Transactional
    public Optional<Product> delete(Long id) {

        Optional<Product> productOptional = repository.findById(id);

        productOptional.ifPresent( p -> {
            repository.delete(p);
        });

        return productOptional;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }


}
