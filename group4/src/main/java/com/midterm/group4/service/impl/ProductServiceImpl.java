package com.midterm.group4.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Page<Product> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC),sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Product findById(UUID id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()) return optProduct.get();
        return null;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(UUID id, Product product) {
        Product findProduct = findById(id);
        if (findProduct != null){
            findProduct.setName(product.getName());
            findProduct.setActive(product.isActive());
            findProduct.setPrice(product.getPrice());
            findProduct.setQuantity(product.getQuantity());
            findProduct.setUpdatedTime(product.getUpdatedTime());
            productRepository.save(findProduct);
        }
        return findProduct;
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, boolean status) {
        Product findProduct = findById(id);
        if (findProduct != null){
            findProduct.setActive(status);
        }
    }

    // @Override
    // @Transactional
    // public List<Product> findAllByName(String name, String sortBy, String sortOrder) {
    //     return productRepository.findAllByName(name, sortBy, sortOrder);
    // }

    // @Override
    // @Transactional
    // public List<Product> findAllByStatus(boolean status, String sortBy, String sortOrder) {
    //     return productRepository.findAllByStatus(status, sortBy, sortOrder);
    // }

}
