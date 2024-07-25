package com.midterm.group4.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private UUID productId;
    private String name;
    private BigInteger price;
    private Integer quantity;
    private boolean isActive;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<OrderItemDTO> listOrderItem;
}
