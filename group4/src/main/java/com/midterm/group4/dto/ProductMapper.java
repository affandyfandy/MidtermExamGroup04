package com.midterm.group4.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.midterm.group4.data.model.Product;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface ProductMapper {
    @Mapping(target = "productId", ignore = true)
    Product toEntity(ProductDTO dto);

    ProductDTO toDto(Product product);
    List<ProductDTO> toListDto(List<Product> products);

    List<Product> toListEntity(List<ProductDTO> listProductDTOs);
    // default Page<ProductDTO> toDto(Page<Product> pageProduct) {
    //     List<ProductDTO> productDtos = pageProduct.stream()
    //             .map(this::toDto)
    //             .collect(Collectors.toList());
    //     return new PageImpl<>(productDtos, pageProduct.getPageable(), pageProduct.getTotalElements());
    // }
}
