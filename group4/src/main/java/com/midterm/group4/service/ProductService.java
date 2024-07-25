package com.midterm.group4.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
// import org.springframework.web.multipart.MultipartFile;
import com.midterm.group4.data.model.Product;

public interface ProductService {

    Page<Product> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Product findById(UUID id);
    Product save(Product product);
    List<Product> saveAll(List<Product> products);
    Product update(UUID id, Product product);
    void updateStatus(UUID id, boolean status);
    Page<Product> findAllByQuery(int pageNo, int pageSize, String sortBy, String sortOrder, String name, String status);
    // Page<Product> findAllByName(int pageNo, int pageSize, String sortBy, String sortOrder, String name);
    // Page<Product> findAllByStatus(int pageNo, int pageSize, String sortBy, String sortOrder, boolean status);
    // List<Product> exportToExcel(MultipartFile file);
    // List<Product> findAllByName(String name, String sortBy, String sortOrder);
    // List<Product> findAllByStatus(boolean status, String sortBy, String sortOrder);

}
