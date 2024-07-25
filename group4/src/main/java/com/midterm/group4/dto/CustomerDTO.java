package com.midterm.group4.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID customerId;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime updatedTime = LocalDateTime.now();
    private boolean isActive;
    private List<InvoiceDTO> listInvoice;
}