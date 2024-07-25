package com.midterm.group4.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.midterm.group4.data.model.Product;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{

}
