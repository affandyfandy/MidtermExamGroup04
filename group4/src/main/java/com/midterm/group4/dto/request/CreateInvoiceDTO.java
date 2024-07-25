package com.midterm.group4.dto.request;

import org.hibernate.validator.constraints.UUID;

import com.midterm.group4.dto.response.ReadOrderItemDTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceDTO {
    private UUID customerId;
    private List<CreateOrderItemDTO> listOrderItem;

}
