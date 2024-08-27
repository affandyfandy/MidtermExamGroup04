package com.midterm.group4.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDTO {
    
    private String firstName;
    private String lastName;
    private String phone;

}