package com.midterm.group4.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.dto.OrderItemMapper;
import com.midterm.group4.dto.request.CreateInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.InvoiceService;

import java.io.IOException;


import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/invoice")
@Tag(name = "Invoice Management", description = "APIs for managing invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Operation(summary = "Get all invoices with sorting", description = "Retrieve all invoices with sorting and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices")
    })
    @GetMapping
    public ResponseEntity<Page<ReadInvoiceDTO>> getAllInvoice(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "totalAmount", required = false) String sortBy

    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        List<ReadInvoiceDTO> invoiceDTOs = pageInvoice.getContent().stream()
            .map(invoice -> invoiceMapper.toReadInvoiceDto(invoice))
            .collect(Collectors.toList());
        Page<ReadInvoiceDTO> pageInvoiceDTO = new PageImpl<>(invoiceDTOs, pageInvoice.getPageable(), pageInvoice.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(pageInvoiceDTO);
    }

    @Operation(summary = "Filter invoices", description = "Filter invoices by date or month with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices"),
            @ApiResponse(responseCode = "400", description = "Invalid request if neither date nor month is provided")
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<ReadInvoiceDTO>> filterInvoice(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "totalAmount", required = false) String sortBy,
        @RequestParam(value = "customerId", required = false) UUID customerId,
        @RequestParam(value = "invoiceDate", required = false) String invoiceDate,
        @RequestParam(value = "month", required = false) String month
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllFiltered(pageNo, pageSize, sortBy, sortOrder, customerId, invoiceDate, month);
        List<ReadInvoiceDTO> invoiceDTOs = pageInvoice.getContent().stream()
            .map(invoice -> invoiceMapper.toReadInvoiceDto(invoice))
            .collect(Collectors.toList());
        Page<ReadInvoiceDTO> pageInvoiceDTO = new PageImpl<>(invoiceDTOs, pageInvoice.getPageable(), pageInvoice.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(pageInvoiceDTO);
    }

    @Operation(summary = "Search invoices by customer name", description = "Search invoices by customer name with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices"),
            @ApiResponse(responseCode = "400", description = "Invalid request if customer name is not provided")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ReadInvoiceDTO>> getInvoiceByCustomerName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(defaultValue = "totalAmount", required = false) String sortBy,
            @RequestParam(required = true) String customerName
    ) {
        if (customerName != null && !customerName.isEmpty()) {
            Page<Invoice> pageInvoice = invoiceService.findAllByCustomerName(pageNo, pageSize, sortBy, sortOrder, customerName);
            List<ReadInvoiceDTO> invoiceDTOs = pageInvoice.getContent().stream()
                .map(invoice -> invoiceMapper.toReadInvoiceDto(invoice))
                .collect(Collectors.toList());
            Page<ReadInvoiceDTO> pageInvoiceDTO = new PageImpl<>(invoiceDTOs, pageInvoice.getPageable(), pageInvoice.getTotalElements());
            return ResponseEntity.status(HttpStatus.OK).body(pageInvoiceDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Get invoice by ID", description = "Retrieve an invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceDTO> getInvoiceById(@PathVariable UUID id)throws ObjectNotFoundException  {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toReadInvoiceDto(invoice));
    }

    @Operation(summary = "Update invoice", description = "Update an existing invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Invoice updated successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadInvoiceDTO> updateInvoice(@PathVariable UUID id, @RequestBody CreateInvoiceDTO invoiceDto) throws ObjectNotFoundException {
        List<OrderItem> listOrderItem = orderItemMapper.toListEntity(invoiceDto.getListOrderItem());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice updatedInvoice = invoiceService.update(id, invoice, listOrderItem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toReadInvoiceDto(updatedInvoice));
    }

    @Operation(summary = "Create new invoice", description = "Create a new invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Invoice created successfully")
    })
    @PostMapping
    public ResponseEntity<ReadInvoiceDTO> addNewInvoice(@RequestBody CreateInvoiceDTO invoiceDto) {
        List<OrderItem> listOrderItem = orderItemMapper.toListEntity(invoiceDto.getListOrderItem());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice newInvoice = invoiceService.createInvoice(invoice, listOrderItem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toReadInvoiceDto(newInvoice));
    }

    @Operation(summary = "Download invoice", description = "Generate invoice detail to pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice downloaded successfully")
    })
    @GetMapping("/{id}/export")
    public ResponseEntity<?> download(@PathVariable UUID id) throws IOException {
        byte[] pdfBytes = invoiceService.generateToPdf(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
            .body(pdfBytes);
    }

    @Operation(summary = "Get report", description = "Get a report of invoices by date, month, or year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of report"),
            @ApiResponse(responseCode = "400", description = "Invalid request if no parameters are provided")
    })
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0", required = false) int month,
            @RequestParam(defaultValue = "0", required = false) int year) {
        Map<String, Object> report = invoiceService.generateReport(date, month, year);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @Operation(summary = "Export invoices to Excel", description = "Export list of invoices to excel by given filter: customer, month, year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful export of invoices")
    })
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportInvoicesToExcel(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) throws IOException {

        List<Invoice> invoices = invoiceService.generateInvoiceReportData(customerId, month, year);
        ByteArrayOutputStream outputStream = invoiceService.exportInvoiceToExcel(invoices);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoices.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.size())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}