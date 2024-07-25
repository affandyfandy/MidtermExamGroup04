package com.midterm.group4.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.midterm.group4.data.model.Customer;

@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface CustomerMapper {
    @Mapping(target = "customerId", ignore = true)
    Customer toEntity(CustomerDTO dto);
    
    CustomerDTO toDto(Customer customer);
    List<CustomerDTO> toListDto(List<Customer> customers);}
