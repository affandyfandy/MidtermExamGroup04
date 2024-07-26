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

    // @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId", nativeQuery = true)
    // Page<Invoice> findAllByCustomerId(@Param("customerId") UUID customerId, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice WHERE DATE(invoice_date) = :invoiceDate", nativeQuery = true)
    // Page<Invoice> findAllByInvoiceDate(@Param("invoiceDate") String invoiceDate, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice WHERE MONTH(invoice_date) = :month", nativeQuery = true)
    // Page<Invoice> findAllByMonth(@Param("month") String month, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId AND DATE(invoice_date) = :invoiceDate", nativeQuery = true)
    // Page<Invoice> findAllByCustomerIdAndInvoiceDate(@Param("customerId") UUID customerId, @Param("invoiceDate") String invoiceDate, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice WHERE customer_id = :customerId AND MONTH(invoice_date) = :month", nativeQuery = true)
    // Page<Invoice> findAllByCustomerIdAndMonth(@Param("customerId") UUID customerId, @Param("month") String month, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice WHERE DATE(invoice_date) = :invoiceDate AND MONTH(invoice_date) = :month", nativeQuery = true)
    // Page<Invoice> findAllByInvoiceDateAndMonth(@Param("invoiceDate") String invoiceDate, @Param("month") String month, Pageable pageable);

    // @Query(value = "SELECT * FROM invoice " +
    //                "WHERE (:customerId IS NULL OR customer_id = :customerId) " +
    //                "AND (:invoiceDate IS NULL OR DATE(created_time) = :invoiceDate) " +
    //                "AND (:month IS NULL OR MONTH(created_time) = :month)",
    //        nativeQuery = true)
    // Page<Invoice> findAllFiltered(@Param("customerId") UUID customerId,
    //                               @Param("invoiceDate") String invoiceDate,
    //                               @Param("month") String month,
    //                               Pageable pageable);



    @Query("SELECT i FROM Invoice i WHERE i.customer.firstName LIKE %:customerName% OR i.customer.lastName LIKE %:customerName%")
    Page<Invoice> findAllByCustomerName(@Param("customerName") String customerName, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId")
    Page<Invoice> findAllByCustomerId(@Param("customerId") UUID customerId, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('DATE', i.invoiceDate) = :invoiceDate")
    Page<Invoice> findAllByInvoiceDate(@Param("invoiceDate") String invoiceDate, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('MONTH', i.invoiceDate) = :month")
    Page<Invoice> findAllByMonth(@Param("month") String month, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId AND FUNCTION('DATE', i.invoiceDate) = :invoiceDate")
    Page<Invoice> findAllByCustomerIdAndInvoiceDate(@Param("customerId") UUID customerId, @Param("invoiceDate") String invoiceDate, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId AND FUNCTION('MONTH', i.invoiceDate) = :month")
    Page<Invoice> findAllByCustomerIdAndMonth(@Param("customerId") UUID customerId, @Param("month") String month, Pageable pageable);
    
    @Query("SELECT i FROM Invoice i WHERE FUNCTION('DATE', i.invoiceDate) = :invoiceDate AND FUNCTION('MONTH', i.invoiceDate) = :month")
    Page<Invoice> findAllByInvoiceDateAndMonth(@Param("invoiceDate") String invoiceDate, @Param("month") String month, Pageable pageable);

    @Query("SELECT i FROM Invoice i " +
        "WHERE (:customerId IS NULL OR i.customer.id = :customerId) " +
        "AND (:invoiceDate IS NULL OR FUNCTION('DATE', i.invoiceDate) = :invoiceDate) " +
        "AND (:month IS NULL OR FUNCTION('MONTH', i.invoiceDate) = :month)")
    Page<Invoice> findAllFiltered(@Param("customerId") UUID customerId,
                            @Param("invoiceDate") String invoiceDate,
                            @Param("month") String month,
                            Pageable pageable);
}
