package com.midterm.group4.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
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
        product.setActive(true);
        product.setListOrderItem(new ArrayList<>());
        product.setCreatedTime(LocalDateTime.now());
        product.setUpdatedTime(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(UUID id, Product product) {
        Product findProduct = findById(id);
        if (findProduct != null){
            findProduct.setName(product.getName());
            findProduct.setPrice(product.getPrice());
            findProduct.setQuantity(product.getQuantity());
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
            findProduct.setUpdatedTime(LocalDateTime.now());
            productRepository.save(findProduct);
        }
    }

    @Override
    @Transactional
    public Page<Product> findAllByQuery(int pageNo, int pageSize, String sortOrder, String sortBy, String name, String status) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy != null ? sortBy : "name");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        int intStatus = 0;
        boolean stat = false;
        if (status != null && "active".equalsIgnoreCase(status)) {
            intStatus = 1;
            stat = true;
        };
        if (name != null && !name.isEmpty() && (status == null || status.isEmpty())) {
            return productRepository.findAllByName(name, pageable);
        } else if (status != null && !status.isEmpty() && (name == null || name.isEmpty())) {
            return productRepository.findAllByStatus(intStatus, pageable);
        } else if (name != null && !name.isEmpty() && status != null && !status.isEmpty()) {
            return productRepository.findAllByNameAndStatus(name, stat, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

}
