package com.midterm.group4.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midterm.group4.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    
}
