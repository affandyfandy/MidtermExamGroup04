package com.midterm.group4.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDTO {
    private UUID orderItemId;
    private UUID productId;
    private int quantity;
    private BigDecimal amount;
}
