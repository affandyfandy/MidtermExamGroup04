package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface InvoiceService {

    Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder);

    Page<Invoice> findAllFiltered(int pageNo, int pageSize, String sortBy, String sortOrder, UUID customerId, String invoiceDate, String month);
    Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem);
    Page<Invoice> findAllByCustomerName(int pageNo, int pageSize, String sortBy, String sortOrder, String name);
    Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder);
    Invoice findById(UUID id);
    Invoice createInvoice(Invoice invoiceDto, List<OrderItem> listOrderItem);
    byte[] generateToPdf(UUID id) throws IOException;
    List<Invoice> generateInvoiceReportData(UUID customerId, Integer month, Integer year);
    Map<String, Object> generateReport(LocalDate date, int month, int year);
    ByteArrayOutputStream exportInvoiceToExcel(List<Invoice> listInvoice) throws IOException;
}