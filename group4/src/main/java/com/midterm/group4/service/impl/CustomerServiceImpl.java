package com.midterm.group4.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.exception.InvalidInputException;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public Page<Customer> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return customerRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Customer findById(UUID id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        validatePhoneNumber(customer.getPhone());
        customer.setActive(true);
        customer.setListInvoice(new ArrayList<>());
        customer.setCreatedTime(LocalDateTime.now());
        customer.setUpdatedTime(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, boolean status) {
        Customer findCustomer = findById(id);
        if (findCustomer != null) {
            findCustomer.setActive(status);
            findCustomer.setUpdatedTime(LocalDateTime.now());
        }
    }

    @Override
    @Transactional
    public Customer update(UUID id, Customer customer) {
        Customer findCustomer = findById(id);
        if (findCustomer != null){
            validatePhoneNumber(customer.getPhone());
            findCustomer.setFirstName(customer.getFirstName());
            findCustomer.setLastName(customer.getLastName());
            findCustomer.setPhone(customer.getPhone());
            customerRepository.save(findCustomer);
        }
        return findCustomer;
    }

    private void validatePhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidInputException("Phone number cannot be null or empty.");
        }

        // Regex for phone number validation (adjust as needed)
        String phoneNumberRegex = "^\\+?[0-9]{10,15}$";
        if (!phone.matches(phoneNumberRegex)) {
            throw new InvalidInputException("Invalid phone number format.");
        }
    }
}
