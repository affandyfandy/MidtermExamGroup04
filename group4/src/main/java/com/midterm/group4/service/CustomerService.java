package com.midterm.group4.service;

import com.midterm.group4.data.model.Customer;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface CustomerService {

    Page<Customer> findAll(int pageNo, int pageSize);
    Customer findById(UUID id);
    Customer saveCustomer(Customer customer);
    void updateStatus(UUID id, boolean status);
    Customer update(UUID id, Customer customer);
}
