package com.midterm.group4.dto.response;

import java.util.UUID;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadOrderItemDTO {
    private UUID orderItemId;
    private ReadCustomerOrderDTO customer;
    private ReadInvoiceOrderDTO invoice;
    private List<ReadProductOrderDTO> listProduct;
}