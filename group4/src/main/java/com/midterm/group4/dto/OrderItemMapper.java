package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}
