package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.midterm.group4.data.model.Product;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface ProductMapper {
    @Mapping(target = "productId", ignore = true)
    Product toEntity(ProductDTO dto);

    ProductDTO toDto(Product product);
    List<ProductDTO> toListDto(List<Product> products);

    List<Product> toListEntity(List<ProductDTO> listProductDTOs);
}
