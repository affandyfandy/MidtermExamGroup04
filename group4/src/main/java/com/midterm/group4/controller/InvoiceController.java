package com.midterm.group4.controller;

import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.InvoiceDTO;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.service.InvoiceService;
import java.util.UUID;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoice(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
            @RequestParam(defaultValue = "createdTime", required = false) String sortBy
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByMonth(
            @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false ) String sortOrder,
            @RequestParam(required = true) int month
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllByMonth(pageNo, pageSize, month, sortOrder);
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
        invoice.setInvoiceDate(null);
        invoice.setCreatedTime(null);
        invoice.setUpdatedTime(null);
        Invoice newInvoice = invoiceService.save(invoice);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toDto(newInvoice));
    }

}
