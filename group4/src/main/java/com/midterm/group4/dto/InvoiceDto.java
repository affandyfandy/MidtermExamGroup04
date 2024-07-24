package com.midterm.group4.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvoiceDto {
    private Integer totalAmount;
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime updatedTime = LocalDateTime.now();
    
}
