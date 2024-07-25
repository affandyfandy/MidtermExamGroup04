package com.midterm.group4.data.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoiceId")
    private UUID invoiceId;

    @Column(name = "totalAmount", nullable = false)
    private BigInteger totalAmount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "createdTime", nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "updatedTime", nullable = false)
    private LocalDateTime updatedTime;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> listOrderItem;

    @PrePersist
    protected void onCreate() {
        if (invoiceId == null) {
            invoiceId = UUID.randomUUID();
        }
    }

}