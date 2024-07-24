package com.midterm.group4.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.midterm.group4.data.model.Product;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
    List<Product> findByName(String name);
}
