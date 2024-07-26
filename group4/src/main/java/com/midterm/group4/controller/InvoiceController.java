package com.midterm.group4.controller;

import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.dto.OrderItemMapper;
import com.midterm.group4.dto.request.CreateInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import com.midterm.group4.service.InvoiceService;
import java.util.UUID;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private OrderItemMapper orderItemMapper;

    @GetMapping
    public ResponseEntity<List<ReadInvoiceDTO>> getAllInvoice(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "totalAmount", required = false) String sortBy
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListReadInvoiceDto(pageInvoice.getContent()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReadInvoiceDTO>> filterInvoice(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "totalAmount", required = false) String sortBy,
        @RequestParam(value = "customerId", required = false) UUID customerId,
        @RequestParam(value = "invoiceDate", required = false) String invoiceDate,
        @RequestParam(value = "month", required = false) String month
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllFiltered(pageNo, pageSize, sortBy, sortOrder, customerId, invoiceDate, month);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListReadInvoiceDto(pageInvoice.getContent()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReadInvoiceDTO>> filterInvoice(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize,
        @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
        @RequestParam(defaultValue = "totalAmount", required = false) String sortBy,
        @RequestParam(value = "customerName", required = false) String name
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllByCustomerName(pageNo, pageSize, sortBy, sortOrder, name);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListReadInvoiceDto(pageInvoice.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceDTO> getInvoiceById(@PathVariable UUID id) {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toReadInvoiceDto(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadInvoiceDTO> updateInvoice(@PathVariable UUID id, @RequestBody CreateInvoiceDTO invoiceDto) {
        List<OrderItem> listOrderItem = orderItemMapper.toListEntity(invoiceDto.getListOrderItem());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice updatedInvoice = invoiceService.update(id, invoice, listOrderItem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toReadInvoiceDto(updatedInvoice));
    }

    @PostMapping
    public ResponseEntity<ReadInvoiceDTO> addNewInvoice(@RequestBody CreateInvoiceDTO invoiceDto) {
        List<OrderItem> listOrderItem = orderItemMapper.toListEntity(invoiceDto.getListOrderItem());
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice newInvoice = invoiceService.createInvoice(invoice, listOrderItem);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toReadInvoiceDto(newInvoice));
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<?> download(@PathVariable UUID id) throws IOException {
        byte[] pdfBytes = invoiceService.generateToPdf(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
            .body(pdfBytes);
    }

}
