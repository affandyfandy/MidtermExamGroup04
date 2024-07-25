package com.midterm.group4.data.model;

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
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customerId")
    private UUID customerId;

    @Column(name = "phone", length = 12, nullable = false)
    private String phone;

    @Column(name = "firstName", length = 100, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 100, nullable = false)
    private String lastName;

    @Column(name = "createdTime", nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "updatedTime", nullable = false)
    private LocalDateTime updatedTime;

    @Column(name = "isActive")
    private boolean isActive;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Invoice> listInvoice;

    @PrePersist
    protected void onCreate() {
        if (customerId == null) {
            customerId = UUID.randomUUID();
        }
    }
}
