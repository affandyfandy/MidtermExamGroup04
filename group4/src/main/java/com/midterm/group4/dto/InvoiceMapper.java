package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.midterm.group4.data.model.Invoice;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface InvoiceMapper {
    @Mapping(target = "customerId", source = "customer.customerId" )
    InvoiceDTO toDto(Invoice invoice);

    List<InvoiceDTO> toListDto(List<Invoice> listInvoices);

    @Mapping(target = "customer.customerId", source = "customerId" )
    Invoice toEntity(InvoiceDTO invoiceDto);
}
