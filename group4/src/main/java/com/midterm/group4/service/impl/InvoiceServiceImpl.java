package com.midterm.group4.service.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.OrderItemRepository;
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

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    @Transactional
    public Invoice createInvoice(InvoiceDTO invoiceDto) {
        Customer customer = customerService.findById(invoiceDto.getCustomerId());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoice.setCustomer(customer);
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public List<Invoice> findByCustomerName(String customerName) {
        return invoiceRepository.findByCustomerNameContaining(customerName);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerDay(LocalDate date) {
        return invoiceRepository.findTotalAmountByDate(date);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerMonth(int month, int year) {
        return invoiceRepository.findTotalAmountByMonth(month, year);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerYear(int year) {
        return invoiceRepository.findTotalAmountByYear(year);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getTop3ProductsByAmount() {
        return orderItemRepository.findTopProductsByAmount().stream()
                .map(data -> Map.of("name", data[0], "totalAmount", data[1]))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<String> getSoldProducts() {
        return orderItemRepository.findSoldProducts();
    }

    @Override
    @Transactional
    public Map<String, Long> getTotalQuantityPerProduct() {
        return orderItemRepository.findTotalQuantityPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> ((Number) data[1]).longValue()
                ));
    }

    @Override
    @Transactional
    public Map<String, BigInteger> getTotalAmountPerProduct() {
        return orderItemRepository.findTotalAmountPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> (BigInteger) data[1]
                ));
    }

    @Override
    @Transactional
    public List<Invoice> getInvoicesByFilter(UUID customerId, Integer month, Integer year) {
        if (customerId != null && month != null && year != null) {
            return invoiceRepository.findByCustomerAndMonthAndYear(customerId, month, year);
        } else if (customerId != null && month != null) {
            return invoiceRepository.findByCustomerAndMonth(customerId, month);
        } else if (customerId != null && year != null) {
            return invoiceRepository.findByCustomerAndYear(customerId, year);
        } else if (month != null && year != null) {
            return invoiceRepository.findByMonthAndYear(month, year);
        } else if (customerId != null) {
            return invoiceRepository.findByCustomer(customerId);
        } else if (year != null) {
            return invoiceRepository.findByYear(year);
        } else {
            return invoiceRepository.findAll();
        }
    }
}
