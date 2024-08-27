package com.midterm.group4.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.midterm.group4.data.model.Product;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{

    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findAllByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.isActive = :status")
    Page<Product> findAllByStatus(@Param("status") boolean status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.isActive = :status")
    Page<Product> findAllByNameAndStatus(@Param("name") String name, @Param("status") boolean status, Pageable pageable);
    
}