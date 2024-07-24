package com.midterm.group4.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private UUID invoiceId;

    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    private LocalDateTime createdTime;
    
    @Column(nullable = false)
    private LocalDateTime updatedTime;

    // @Column(nullable = false)
    // private Customer customer;

    // @Column(nullable = false)
    // private List<OrderLine> listOrder;
}
