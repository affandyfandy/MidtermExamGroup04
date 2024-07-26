package com.midterm.group4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadInvoiceOrderDTO {
    private UUID invoiceId;
    private BigInteger totalAmount;
    private LocalDate invoiceDate;
}
