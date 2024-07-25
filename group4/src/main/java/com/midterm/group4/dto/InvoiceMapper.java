package com.midterm.group4.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface InvoiceMapper {
    @Mapping(target = "customerId", ignore = true )
    InvoiceDTO toDto(Invoice invoice);

    List<InvoiceDTO> toListDto(List<Invoice> listInvoices);

    @Mapping(target = "customer.customerId", source = "customerId" )
    @Mapping(target = "invoiceId", ignore = true )
    Invoice toEntity(InvoiceDTO invoiceDto);
}
