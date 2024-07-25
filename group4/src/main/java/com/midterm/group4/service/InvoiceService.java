package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.midterm.group4.dto.InvoiceDTO;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);
    Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate sortBy, String sortOrder);
    Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder);
    Invoice findById(UUID id);
    Invoice save(Invoice invoice);
    Invoice update(UUID id, Invoice product);
    Invoice createInvoice(InvoiceDTO invoiceDto);
    byte[] generateToPdf(UUID id) throws IOException;
}
