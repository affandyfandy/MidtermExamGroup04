package com.midterm.group4.service;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportInvoicesToExcel(List<Invoice> invoices) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Invoices");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Invoice ID", "Customer ID", "Customer Name", "Amount", "Product ID", "Product Name", "Product Price", "Product Quantity", "Product Amount"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Invoice invoice : invoices) {
                for (OrderItem orderItem : invoice.getListOrderItem()) {
                    Row row = sheet.createRow(rowIdx++);

                    row.createCell(0).setCellValue(invoice.getInvoiceId().toString());
                    row.createCell(1).setCellValue(invoice.getCustomer().getCustomerId().toString());
                    row.createCell(2).setCellValue(invoice.getCustomer().getFirstName() + " " + invoice.getCustomer().getLastName());
                    row.createCell(3).setCellValue(invoice.getTotalAmount().toString());
                    row.createCell(4).setCellValue(orderItem.getProduct().getProductId().toString());
                    row.createCell(5).setCellValue(orderItem.getProduct().getName());
                    row.createCell(6).setCellValue(orderItem.getProduct().getPrice().toString());
                    row.createCell(7).setCellValue(orderItem.getQuantity());
                    row.createCell(8).setCellValue(orderItem.getAmount().toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}
