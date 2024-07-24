package com.midterm.group4.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public void create(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void update(Long id, Product dataProduct) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()){
            Product product = optProduct.get();
            product.setPrice(dataProduct.getPrice());
            product.setName(dataProduct.getName());
            product.setQuantity(dataProduct.getQuantity());
            product.setStock(dataProduct.getStock());
            product.setUpdatedTime(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()){
            Product product = optProduct.get();
            productRepository.delete(product);
        }
    }

    @Override
    @Transactional
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public List<Product> findAllByName(String name){
        return productRepository.findByName(name);
    }
    
}
