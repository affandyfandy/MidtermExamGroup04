package com.midterm.group4.dto;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderItemDTO {
    private UUID orderItemId;
    private UUID productId;
    private UUID invoiceId;
    private Integer quantity;
    private BigInteger amount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
