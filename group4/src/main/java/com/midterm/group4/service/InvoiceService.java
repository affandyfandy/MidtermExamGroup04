package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import java.io.IOException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);

    Page<Invoice> findAllFiltered(int pageNo, int pageSize, String sortBy, String sortOrder, UUID customerId, String invoiceDate, String month);
    Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem);
    Page<Invoice> findAllByCustomerName(int pageNo, int pageSize, String sortBy, String sortOrder, String name);
    Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate sortBy, String sortOrder);
    Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder);
    Page<Invoice> findAll(int pageNo, int pageSize, String sortBy);
    Invoice findById(UUID id);
    Invoice save(Invoice invoice);
    Invoice createInvoice(Invoice invoiceDto, List<OrderItem> listOrderItem);
    BigInteger getTotalAmountPerDay(LocalDate date);
    BigInteger getTotalAmountPerMonth(int month, int year);
    BigInteger getTotalAmountPerYear(int year);
    List<Map<String, Object>> getTop3ProductsByAmount();
    List<String> getSoldProducts();
    Map<String, Long> getTotalQuantityPerProduct();
    Map<String, BigInteger> getTotalAmountPerProduct();
    byte[] generateToPdf(UUID id) throws IOException;
    List<Invoice> getInvoicesByFilter(UUID customerId, Integer month, Integer year);
}
