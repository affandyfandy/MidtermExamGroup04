package com.midterm.group4.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.request.CreateCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerOrderDTO;

@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface CustomerMapper {

    ReadCustomerDTO toReadDto(Customer customer);

    Customer toEntity(CreateCustomerDTO customerDTO);

    List<ReadCustomerDTO> toListReadDto(List<Customer> customers);
   
    ReadCustomerOrderDTO toReadCustomerOrderDto(Customer customer);
}
