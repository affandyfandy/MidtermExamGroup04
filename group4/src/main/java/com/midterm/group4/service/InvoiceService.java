package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    
    void create(Invoice invoice);
    Invoice update(UUID id, Invoice dataInvoice);
    void delete(UUID id);
    List<Invoice> findAll();
    Invoice findById(UUID id);
    // List<Invoice> findAllByCustomer(Customer cust);
}
