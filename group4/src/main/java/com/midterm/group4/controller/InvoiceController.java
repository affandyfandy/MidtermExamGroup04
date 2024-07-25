package com.midterm.group4.controller;

import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.OrderItemRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.InvoiceDTO;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.service.InvoiceService;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/sort")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoiceSort(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(required = true) String sortBy
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoice(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false ) String sortOrder
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAll(pageNo, pageSize, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByFilter(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
            @RequestParam(required = false) LocalDate invoiceDate,
            @RequestParam(required = false) Integer month
    ) {
        Page<Invoice> pageInvoice;
        if (invoiceDate != null) {
            pageInvoice = invoiceService.findAllByDate(pageNo, pageSize, invoiceDate, sortOrder);
        } else if (month != null) {
            pageInvoice = invoiceService.findAllByMonth(pageNo, pageSize, month, sortOrder);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid request if neither is provided
        }
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByCustomerName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
            @RequestParam(required = true) String customerName
    ) {
        Page<Invoice> pageInvoice;
        if (customerName != null && !customerName.isEmpty()) {
            List<Invoice> invoices = invoiceService.findByCustomerName(customerName);
            pageInvoice = new PageImpl<>(invoices); // Convert to Page if needed
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid request if neither is provided
        }
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toDto(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable UUID id, @RequestBody InvoiceDTO invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice updatedInvoice = invoiceService.update(id, invoice);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toDto(updatedInvoice));
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> addNewInvoice(@RequestBody InvoiceDTO invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        // invoice.setInvoiceDate(null);
        // invoice.setCreatedTime(null);
        // invoice.setUpdatedTime(null);
        Invoice newInvoice = invoiceService.save(invoice);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toDto(newInvoice));
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        if (date == null && month == null && year == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "At least one parameter (date, month, year) is required"));
        }

        BigInteger totalAmountPerDay = date != null ? invoiceService.getTotalAmountPerDay(date) : null;
        BigInteger totalAmountPerMonth = (month != null && year != null) ? invoiceService.getTotalAmountPerMonth(month, year) : null;
        BigInteger totalAmountPerYear = year != null ? invoiceService.getTotalAmountPerYear(year) : null;

        List<Map<String, Object>> topProductsByAmount = invoiceService.getTop3ProductsByAmount();
        List<String> soldProducts = invoiceService.getSoldProducts();
        Map<String, Long> totalQuantityPerProduct = invoiceService.getTotalQuantityPerProduct();
        Map<String, BigInteger> totalAmountPerProduct = invoiceService.getTotalAmountPerProduct();

        Map<String, Object> report = new LinkedHashMap<>();
        if (date != null) {
            report.put("Revenue generated this day", totalAmountPerDay);
        }
        if (month != null) {
            report.put("Revenue generated this month", totalAmountPerMonth);
        }
        if (year != null && month == null) {
            report.put("Revenue generated this year", totalAmountPerYear);
        }
        report.put("Top 3 Products By Amount", topProductsByAmount);
        report.put("Product Quantity Sold", totalQuantityPerProduct);
        report.put("Revenue Per Product", totalAmountPerProduct);
        report.put("Sold Products", soldProducts);

        return ResponseEntity.ok(report);
    }
}
