package com.midterm.group4.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import com.midterm.group4.data.model.Product;

public interface ProductService {

    Page<Product> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Product findById(UUID id);
    Product save(Product product);
    void saveAll(List<Product> products);
    Product update(UUID id, Product product);
    Product updateStatus(UUID id, boolean status);
    Page<Product> findAllByQuery(int pageNo, int pageSize, String sortBy, String sortOrder, String name, String status);
}