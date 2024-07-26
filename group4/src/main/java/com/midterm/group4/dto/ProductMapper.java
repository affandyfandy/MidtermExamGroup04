package com.midterm.group4.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import com.midterm.group4.dto.response.ReadProductDTO;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface ProductMapper {
    Product toEntity(CreateProductDTO dto);

    // ProductDTO toDto(Product product);
    // List<ProductDTO> toListDto(List<Product> products);

    ReadProductDTO toReadProductDto(Product product);
    List<ReadProductDTO> toListReadProductDto(List<Product> listProducts);
    // List<Product> toListEntity(List<ProductDTO> listProductDTOs);

    // Product toEntity(CreateProductOrderDTO dto);

    // List<Product> toListEntity(List<CreateProductOrderDTO> dto);
}
