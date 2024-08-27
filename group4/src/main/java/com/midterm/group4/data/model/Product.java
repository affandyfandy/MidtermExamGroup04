package com.midterm.group4.data.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostUpdate;
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
    @Column(updatable = false, nullable = false)
    private UUID productId;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigInteger price;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> listOrderItem;

    @PrePersist
    protected void onCreate() {
        // if (productId == null) {
        //     productId = UUID.randomUUID();
        // }
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PostUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}