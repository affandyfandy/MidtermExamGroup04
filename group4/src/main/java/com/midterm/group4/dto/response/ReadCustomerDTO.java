package com.midterm.group4.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadCustomerDTO {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean isActive;
}