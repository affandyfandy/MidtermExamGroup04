package com.midterm.group4.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private boolean isActive;

    public void setIsActive(boolean active) {
    }
}