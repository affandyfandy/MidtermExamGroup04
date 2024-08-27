package com.midterm.group4.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID orderItemId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigInteger amount;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @ManyToOne
    @JoinColumn(name = "invoiceId")
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonIgnore
    private Product product;

    @PrePersist
    protected void onCreate() {
        // if (orderItemId == null) {
        //     orderItemId = UUID.randomUUID();
        // }
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }
    
    @PostUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}