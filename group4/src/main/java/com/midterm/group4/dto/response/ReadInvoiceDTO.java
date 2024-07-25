package com.midterm.group4.dto.response;

import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadInvoiceDTO {
    private UUID invoiceId;
    private BigInteger totalAmount;
    private ReadCustomerOrderDTO customer;
    private LocalDate invoiceDate;
}