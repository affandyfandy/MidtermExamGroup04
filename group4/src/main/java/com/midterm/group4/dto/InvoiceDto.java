package com.midterm.group4.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class InvoiceDTO {
    private UUID invoiceId;
    private UUID customerId;
    private BigInteger totalAmount;
    private Date date;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<OrderItemDTO> listOrderItem;
}
