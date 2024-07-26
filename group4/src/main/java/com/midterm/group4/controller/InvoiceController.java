package com.midterm.group4.controller;

import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.OrderItemRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.InvoiceDTO;
import com.midterm.group4.dto.InvoiceMapper;
import com.midterm.group4.service.InvoiceService;
import com.midterm.group4.service.ExcelExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ExcelExportService excelExportService;

    @Operation(summary = "Get all invoices with sorting", description = "Retrieve all invoices with sorting and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices")
    })
    @GetMapping("/sort")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoiceSort(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(required = true) String sortBy
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAllSorted(pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @Operation(summary = "Get all invoices", description = "Retrieve all invoices with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices")
    })
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoice(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false) String sortOrder
    ) {
        Page<Invoice> pageInvoice = invoiceService.findAll(pageNo, pageSize, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @Operation(summary = "Filter invoices", description = "Filter invoices by date or month with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices"),
            @ApiResponse(responseCode = "400", description = "Invalid request if neither date nor month is provided")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByFilter(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDate,
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

    @Operation(summary = "Search invoices by customer name", description = "Search invoices by customer name with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of invoices"),
            @ApiResponse(responseCode = "400", description = "Invalid request if customer name is not provided")
    })
    @GetMapping("/search")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByCustomerName(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(required = true) String customerName
    ) {
        Page<Invoice> pageInvoice;
        if (customerName != null && !customerName.isEmpty()) {
            List<Invoice> invoices = invoiceService.findByCustomerName(customerName);
            pageInvoice = new PageImpl<>(invoices); // Convert to Page if needed
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid request if customer name is not provided
        }
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toListDto(pageInvoice.getContent()));
    }

    @Operation(summary = "Get invoice by ID", description = "Retrieve an invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceMapper.toDto(invoice));
    }

    @Operation(summary = "Update invoice", description = "Update an existing invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Invoice updated successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable UUID id, @RequestBody InvoiceDTO invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice updatedInvoice = invoiceService.update(id, invoice);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toDto(updatedInvoice));
    }

    @Operation(summary = "Create new invoice", description = "Create a new invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Invoice created successfully")
    })
    @PostMapping
    public ResponseEntity<InvoiceDTO> addNewInvoice(@RequestBody InvoiceDTO invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        Invoice newInvoice = invoiceService.save(invoice);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(invoiceMapper.toDto(newInvoice));
    }

    @Operation(summary = "Get report", description = "Get a report of invoices by date, month, or year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of report"),
            @ApiResponse(responseCode = "400", description = "Invalid request if no parameters are provided")
    })
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

    @Operation(summary = "Export invoices to Excel", description = "Export list of invoices to excel by given filter: customer, month, year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful export of invoices")
    })
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportInvoicesToExcel(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) throws IOException {

        List<Invoice> invoices = invoiceService.getInvoicesByFilter(customerId, month, year);

        // Create a workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Invoices");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Invoice ID");
        headerRow.createCell(1).setCellValue("Customer ID");
        headerRow.createCell(2).setCellValue("Customer Name");
        headerRow.createCell(3).setCellValue("Total Amount");
        headerRow.createCell(4).setCellValue("Products");

        // Fill data rows
        int rowNum = 1;
        for (Invoice invoice : invoices) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(invoice.getInvoiceId().toString());
            row.createCell(1).setCellValue(invoice.getCustomer().getCustomerId().toString());
            row.createCell(2).setCellValue(invoice.getCustomer().getFirstName() + " " + invoice.getCustomer().getLastName());
            row.createCell(3).setCellValue(invoice.getTotalAmount().toString());

            StringBuilder products = new StringBuilder();
            invoice.getListOrderItem().forEach(orderItem -> {
                BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                BigDecimal price = new BigDecimal(orderItem.getProduct().getPrice().toString());
                BigDecimal amount = quantity.multiply(price);

                products.append("ID: ").append(orderItem.getProduct().getProductId().toString())
                        .append(", Name: ").append(orderItem.getProduct().getName())
                        .append(", Price: ").append(price.toString())
                        .append(", Quantity: ").append(quantity.toString())
                        .append(", Amount: ").append(amount.toString())
                        .append("; ");
            });
            row.createCell(4).setCellValue(products.toString());
        }

        // Write to byte array output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Create response headers
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
