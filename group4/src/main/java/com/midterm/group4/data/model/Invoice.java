package com.midterm.group4.data.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID invoiceId;

    @Column(nullable = false)
    private BigInteger totalAmount;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> listOrderItem;

    @PrePersist
    protected void onCreate() {
        // if (invoiceId == null) {
        //     invoiceId = UUID.randomUUID();
        // }
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PostUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}