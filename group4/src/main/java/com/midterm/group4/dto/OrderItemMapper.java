package com.midterm.group4.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigInteger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.midterm.group4.data.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "product.productId", source = "productId")
    @Mapping(target = "invoice.invoiceId", source = "invoiceId")
    @Mapping(target = "orderItemId", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "invoiceId", source = "invoice.invoiceId")
    OrderItemDTO toDto(OrderItem orderItem);
    List<OrderItemDTO> toListDto(List<OrderItem> orderItems);
    // default Page<OrderItemDTO> toDto(Page<OrderItem> pageOrderItem) {
    //     List<OrderItemDTO> orderItemDTOs = pageOrderItem.stream()
    //             .map(this::toDto)
    //             .collect(Collectors.toList());
    //     return new PageImpl<>(orderItemDTOs, pageOrderItem.getPageable(), pageOrderItem.getTotalElements());
    // }
}
