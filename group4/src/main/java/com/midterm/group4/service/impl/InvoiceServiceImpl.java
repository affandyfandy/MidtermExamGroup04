package com.midterm.group4.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.InvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.service.CustomerService;
import com.midterm.group4.service.InvoiceService;
import com.midterm.group4.dto.InvoiceDTO;
import com.midterm.group4.dto.InvoiceMapper;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    @Transactional
    public Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByDate'");
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByMonth'");
    }

    @Override
    @Transactional
    public Invoice findById(UUID id) {
        Optional<Invoice> optInvoice = invoiceRepository.findById(id);
        if (optInvoice.isPresent()) return optInvoice.get();
        return null;
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice update(UUID id, Invoice invoice) {
        Invoice findInvoice = findById(id);
        if (findInvoice != null){
            findInvoice.setUpdatedTime(invoice.getUpdatedTime());
            findInvoice.setTotalAmount(invoice.getTotalAmount());
            invoiceRepository.save(findInvoice);
        }
        return findInvoice;
    }
    @Transactional
    public Invoice createInvoice(InvoiceDTO invoiceDto) {
        Customer customer = customerService.findById(invoiceDto.getCustomerId());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoice.setCustomer(customer);
        return invoiceRepository.save(invoice);
    }
}
