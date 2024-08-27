package com.midterm.group4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductDTO {
    private UUID productId;
    private String name;
    private boolean isActive;
    private BigInteger price;
}