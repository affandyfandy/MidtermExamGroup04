package com.midterm.group4.service.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.dto.InvoiceDTO;
import com.midterm.group4.service.OrderItemService;
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

    @Autowired
    private OrderItemService orderItemService;

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

    @Override
    @Transactional
    public Invoice createInvoice(InvoiceDTO invoiceDto) {
        // Create Invoice entity from DTO
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        // Set Order Items
        List<OrderItem> orderItems = orderItemService.findAllByIds(invoiceDto.getOrderItemId());
        invoice.setOrderItemId(invoiceDto.getOrderItemId());
        // Calculate total amount
        BigInteger totalAmount = orderItems.stream()
                .map(orderItem -> orderItem.getAmount().multiply(BigInteger.valueOf(orderItem.getQuantity())))
                .reduce(BigInteger.ZERO, BigInteger::add);
        invoice.setTotalAmount(totalAmount);
        // Save Invoice
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public BigInteger recalculateTotalAmount(List<UUID> orderItemIds) {
        List<OrderItem> orderItems = orderItemService.findAllByIds(orderItemIds);
        return orderItems.stream()
                .map(orderItem -> orderItem.getAmount().multiply(BigInteger.valueOf(orderItem.getQuantity())))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
