package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.midterm.group4.dto.InvoiceDTO;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate sortBy, String sortOrder);
    Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder);
    Page<Invoice> findAll(int pageNo, int pageSize, String sortBy);
    Invoice findById(UUID id);
    Invoice save(Invoice invoice);
    Invoice update(UUID id, Invoice product);
    Invoice createInvoice(InvoiceDTO invoiceDto);
    List<Invoice> findByCustomerName(String customerName);
    BigInteger getTotalAmountPerDay(LocalDate date);
    BigInteger getTotalAmountPerMonth(int month, int year);
    BigInteger getTotalAmountPerYear(int year);
    List<Map<String, Object>> getTop3ProductsByAmount();
    List<String> getSoldProducts();
    Map<String, Long> getTotalQuantityPerProduct();
    Map<String, BigInteger> getTotalAmountPerProduct();

    List<Invoice> getInvoicesByFilter(UUID customerId, Integer month, Integer year);
}
