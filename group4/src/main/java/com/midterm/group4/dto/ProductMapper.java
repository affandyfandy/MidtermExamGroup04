package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadProductDTO;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface ProductMapper {
    Product toEntity(CreateProductDTO dto);
    ReadProductDTO toReadProductDto(Product product);
    List<ReadProductDTO> toListReadProductDto(List<Product> listProducts);
}