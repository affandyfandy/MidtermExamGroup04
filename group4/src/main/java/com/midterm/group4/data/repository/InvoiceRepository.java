package com.midterm.group4.data.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.midterm.group4.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    
    @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId", nativeQuery = true)
    Page<Invoice> findAllByCustomerId(@Param("customerId") UUID customerId, Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE DATE(invoice_date) = :invoiceDate", nativeQuery = true)
    Page<Invoice> findAllByInvoiceDate(@Param("invoiceDate") String invoiceDate, Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE MONTH(invoice_date) = :month", nativeQuery = true)
    Page<Invoice> findAllByMonth(@Param("month") String month, Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId AND DATE(invoice_date) = :invoiceDate", nativeQuery = true)
    Page<Invoice> findAllByCustomerIdAndInvoiceDate(@Param("customerId") UUID customerId, @Param("invoiceDate") String invoiceDate, Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId AND MONTH(invoice_date) = :month", nativeQuery = true)
    Page<Invoice> findAllByCustomerIdAndMonth(@Param("customerId") UUID customerId, @Param("month") String month, Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE DATE(invoice_date) = :invoiceDate AND MONTH(invoice_date) = :month", nativeQuery = true)
    Page<Invoice> findAllByInvoiceDateAndMonth(@Param("invoiceDate") String invoiceDate, @Param("month") String month, Pageable pageable);

    @Query(value = "SELECT * FROM invoice " +
                   "WHERE (:customerId IS NULL OR customer_id = :customerId) " +
                   "AND (:invoiceDate IS NULL OR DATE(created_time) = :invoiceDate) " +
                   "AND (:month IS NULL OR MONTH(created_time) = :month)",
           nativeQuery = true)
    Page<Invoice> findAllFiltered(@Param("customerId") UUID customerId,
                                  @Param("invoiceDate") String invoiceDate,
                                  @Param("month") String month,
                                  Pageable pageable);
}
