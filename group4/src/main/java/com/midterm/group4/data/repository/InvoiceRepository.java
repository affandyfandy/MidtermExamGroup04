package com.midterm.group4.data.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.midterm.group4.data.model.Invoice;

import com.midterm.group4.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Page<Invoice> findByInvoiceDate(LocalDate invoiceDate, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('MONTH', i.invoiceDate) = :month")
    Page<Invoice> findByMonth(@Param("month") int month, Pageable pageable);
}
