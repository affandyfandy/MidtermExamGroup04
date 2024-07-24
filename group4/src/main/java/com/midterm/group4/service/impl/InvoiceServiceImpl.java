package com.midterm.group4.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public void create(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice update(UUID id, Invoice dataInvoice) {
        Optional<Invoice> optInvoice = invoiceRepository.findById(id);
        if (optInvoice.isPresent()){
            Invoice inv = optInvoice.get();
            inv.setUpdatedTime(LocalDateTime.now());
            inv.setTotalAmount(dataInvoice.getTotalAmount());
            invoiceRepository.save(inv);
        }
        return invoiceRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Optional<Invoice> optInvoice = invoiceRepository.findById(id);
        if (optInvoice.isPresent()){
            invoiceRepository.delete(optInvoice.get());
        }
    }

    @Override
    @Transactional
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    @Transactional
    public Invoice findById(UUID id) {
        Optional<Invoice> optInvoice = invoiceRepository.findById(id);
        if (optInvoice.isPresent()){
            return optInvoice.get();
        }
        return null;
    }
    
}
