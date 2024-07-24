package com.midterm.group4.service.impl;

import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhone(customerDTO.getPhone());
        customer.setActive(customerDTO.isActive());
        customer.setCreatedTime(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setPhone(customerDTO.getPhone());
            customer.setActive(customerDTO.isActive());
            customer.setUpdatedTime(LocalDateTime.now());
            return customerRepository.save(customer);
        } else {
            return null;
        }
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
}
