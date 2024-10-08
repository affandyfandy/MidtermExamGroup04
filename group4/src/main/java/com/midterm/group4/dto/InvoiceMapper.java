package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.request.CreateInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceOrderDTO;
import com.midterm.group4.dto.response.ReadOrderItemDTO;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface InvoiceMapper {

    ReadInvoiceOrderDTO toReadInvoiceOrderDto(Invoice invoice);

    ReadInvoiceDTO toReadInvoiceDto(Invoice invoice);
    
    List<ReadInvoiceDTO> toListReadInvoiceDto(List<Invoice> invoices);

    @Mapping(target = "customer.customerId", source = "customerId")
    Invoice toEntity(CreateInvoiceDTO invoiceDto);


}
