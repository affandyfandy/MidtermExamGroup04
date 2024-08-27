package com.midterm.group4.data.repository;

import com.midterm.group4.data.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("SELECT oi.product.name, SUM(oi.quantity * oi.product.price) as totalAmount FROM OrderItem oi GROUP BY oi.product.name ORDER BY totalAmount DESC")
    List<Object[]> findTopProductsByAmount();

    @Query("SELECT DISTINCT p.name FROM OrderItem oi JOIN oi.product p")
    List<String> findSoldProducts();

    @Query("SELECT p.name, SUM(oi.quantity) FROM OrderItem oi JOIN oi.product p GROUP BY p.name")
    List<Object[]> findTotalQuantityPerProduct();

    @Query("SELECT oi.product.name, SUM(oi.quantity * oi.product.price) as totalAmount FROM OrderItem oi GROUP BY oi.product.name ORDER BY totalAmount DESC")
    List<Object[]> findTotalAmountPerProduct();
}