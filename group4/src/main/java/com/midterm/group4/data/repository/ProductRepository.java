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

    @Query(value = "SELECT * FROM product WHERE name LIKE :name", nativeQuery = true)
    Page<Product> findAllByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE is_active = :status", nativeQuery = true)
    Page<Product> findAllByStatus(@Param("status") int status, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE name LIKE :name AND is_active = :status", nativeQuery = true)
    Page<Product> findAllByNameAndStatus(@Param("name") String name, @Param("status") int status, Pageable pageable);
}
