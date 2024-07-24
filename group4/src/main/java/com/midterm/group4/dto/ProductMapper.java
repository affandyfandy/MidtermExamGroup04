package com.midterm.group4.dto;

import org.mapstruct.Mapper;

import com.midterm.group4.data.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    Product toEntity(ProductDto productDto);
}
