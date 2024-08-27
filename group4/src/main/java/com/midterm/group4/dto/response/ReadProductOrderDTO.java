package com.midterm.group4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductOrderDTO {
    private String name;
    private Integer quantity;
    private BigInteger price;
}