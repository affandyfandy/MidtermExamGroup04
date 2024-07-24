package com.midterm.group4.dto;

import org.mapstruct.Mapper;

import com.midterm.group4.data.model.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDto toDto(Invoice invoice);
    Invoice toEntity(InvoiceDto invoiceDto);
    
}
