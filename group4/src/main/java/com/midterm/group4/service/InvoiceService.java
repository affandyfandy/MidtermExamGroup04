package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Product;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Page<Invoice> findAllFiltered(int pageNo, int pageSize, String sortBy, String sortOrder, UUID customerId, String invoiceDate, String month);
    Invoice findById(UUID id);
    Invoice save(Invoice invoice);
    Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem);
    Invoice createInvoice(Invoice invoiceDto, List<OrderItem> listOrderItem);
    byte[] generateToPdf(UUID id) throws IOException;
}
