package com.midterm.group4.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String phone;
}
