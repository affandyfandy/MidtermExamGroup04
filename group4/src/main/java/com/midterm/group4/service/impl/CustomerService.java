package com.midterm.group4.service.impl;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Page<CustomerDTO> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(this::convertToDTO);
    }

    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return customerRepository.findById(id).map(this::convertToDTO);
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        return convertToDTO(customerRepository.save(customer));
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setCreatedTime(customer.getCreatedTime());
        customerDTO.setUpdatedTime(customer.getUpdatedTime());
        customerDTO.setIsActive(customer.isActive());
        return customerDTO;
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setCreatedTime(customerDTO.getCreatedTime());
        customer.setUpdatedTime(customerDTO.getUpdatedTime());
        customer.setActive(customerDTO.isActive());
        return customer;
    }
}
