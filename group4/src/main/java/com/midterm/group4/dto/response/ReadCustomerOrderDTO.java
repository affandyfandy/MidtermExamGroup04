package com.midterm.group4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadCustomerOrderDTO {
    private UUID customerId;
    private String firstName;
    private String lastName;
}
