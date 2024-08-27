package com.midterm.group4.service;

import com.midterm.group4.data.model.Customer;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface CustomerService {

    Page<Customer> findAll(int pageNo, int pageSize);
    Customer findById(UUID id);
    Customer saveCustomer(Customer customer);
    Customer updateNew(UUID id, Customer customer);
    Customer updateStatusNew(UUID id, boolean status);
}