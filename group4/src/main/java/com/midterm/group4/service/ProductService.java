package com.midterm.group4.service;

import java.util.List;
import com.midterm.group4.data.model.Product;

public interface ProductService {

    void create(Product product);
    void update(Long id, Product dataProduct);
    void delete(Long id);
    List<Product> findAll();
    List<Product> findAllByName(String name);

}
