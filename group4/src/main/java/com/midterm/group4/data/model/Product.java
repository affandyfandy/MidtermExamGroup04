package com.midterm.group4.data.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "productId", updatable = false, nullable = false)
    private UUID productId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigInteger price;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "createdTime", nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "updatedTime", nullable = false)
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> listOrderItem;

    @PrePersist
    protected void onCreate() {
        if (productId == null) {
            productId = UUID.randomUUID();
        }
    }
    
}