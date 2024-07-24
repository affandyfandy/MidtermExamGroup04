package com.midterm.group4.controller;

import org.springframework.web.bind.annotation.RestController;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.InvoiceDto;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.service.InvoiceService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<?> getAllInvoice() {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable UUID id, @RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        return ResponseEntity.ok(invoiceService.update(id, invoice));
    }
    
    @PostMapping
    public ResponseEntity addNewInvoice(@RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoiceService.create(invoice);
        return ResponseEntity.ok("Created successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteInvoice(@PathVariable UUID id){
        invoiceService.delete(id);
        return ResponseEntity.ok("Delete successful");
    }
    
}
