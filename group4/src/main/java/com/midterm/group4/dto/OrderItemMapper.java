package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.dto.request.CreateOrderItemDTO;
import com.midterm.group4.dto.response.ReadOrderItemDTO;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, InvoiceMapper.class})
public interface OrderItemMapper {

    List<ReadOrderItemDTO> toListReadOrderItemDto(List<OrderItem> orderItems);

    @Mapping(target = "product.productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    OrderItem toEntity(CreateOrderItemDTO dto);

    List<OrderItem> toListEntity(List<CreateOrderItemDTO> dto);
}