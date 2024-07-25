package com.midterm.group4.service.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import com.midterm.group4.data.model.OrderItem;
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
    public Page<Invoice> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate invoiceDate, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findByInvoiceDate(invoiceDate, pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findByMonth(month, pageable);
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
        List<OrderItem> listOrderItem = invoice.getListOrderItem();
        BigInteger totalAmount = BigInteger.valueOf(0);
        for (OrderItem order : listOrderItem){
            totalAmount.add(order.getAmount());
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setCreatedTime(LocalDateTime.now());
        invoice.setUpdatedTime(LocalDateTime.now());
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

    // @Override
    // @Transactional
    // public void updateOrder(Invoice invoice, OrderItem orderItem){
    //     List<OrderItem> listOrderItem = invoice.getListOrderItem();

    // }

    @Transactional
    public Invoice createInvoice(InvoiceDTO invoiceDto) {
        Customer customer = customerService.findById(invoiceDto.getCustomerId());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoice.setCustomer(customer);
        return invoiceRepository.save(invoice);
    }
}
