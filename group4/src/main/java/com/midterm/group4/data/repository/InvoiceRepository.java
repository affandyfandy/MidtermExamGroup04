package com.midterm.group4.data.repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
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
// =======
    Page<Invoice> findByInvoiceDate(LocalDate invoiceDate, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('MONTH', i.invoiceDate) = :month")
    Page<Invoice> findByMonth(@Param("month") int month, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.customer.firstName LIKE %:customerName% OR i.customer.lastName LIKE %:customerName%")
    List<Invoice> findByCustomerNameContaining(@Param("customerName") String customerName);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.invoiceDate = :date")
    BigInteger findTotalAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE FUNCTION('MONTH', i.invoiceDate) = :month AND FUNCTION('YEAR', i.invoiceDate) = :year")
    BigInteger findTotalAmountByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE FUNCTION('YEAR', i.invoiceDate) = :year")
    BigInteger findTotalAmountByYear(@Param("year") int year);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('MONTH', i.invoiceDate) = :month AND FUNCTION('YEAR', i.invoiceDate) = :year")
    List<Invoice> findByMonthAndYear(int month, int year);

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId AND FUNCTION('MONTH', i.invoiceDate) = :month AND FUNCTION('YEAR', i.invoiceDate) = :year")
    List<Invoice> findByCustomerAndMonthAndYear(UUID customerId, int month, int year);

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId AND FUNCTION('MONTH', i.invoiceDate) = :month")
    List<Invoice> findByCustomerAndMonth(UUID customerId, int month);

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId AND FUNCTION('YEAR', i.invoiceDate) = :year")
    List<Invoice> findByCustomerAndYear(UUID customerId, int year);

    @Query("SELECT i FROM Invoice i WHERE FUNCTION('YEAR', i.invoiceDate) = :year")
    List<Invoice> findByYear(int year);

    @Query("SELECT i FROM Invoice i WHERE i.customer.customerId = :customerId")
    List<Invoice> findByCustomer(UUID customerId);
}
