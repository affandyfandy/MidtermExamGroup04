package com.midterm.group4.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    // public Page<CustomerDTO> getAllCustomers(int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<Customer> customers = customerRepository.findAll(pageable);
    //     return customers.map(this::convertToDTO);
    // }

    // public Optional<CustomerDTO> getCustomerById(UUID id) {
    //     return customerRepository.findById(id).map(this::convertToDTO);
    // }

    // public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
    //     Customer customer = convertToEntity(customerDTO);
    //     return convertToDTO(customerRepository.save(customer));
    // }

    // public void deleteCustomer(UUID id) {
    //     customerRepository.deleteById(id);
    // }

    @Override
    @Transactional
    public Page<Customer> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return customerRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Customer findById(UUID id) {
        Optional<Customer> optCustomer = customerRepository.findById(id);
        if (optCustomer.isPresent()) return optCustomer.get();
        return null;
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
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
            findCustomer.setActive(customer.isActive());
            findCustomer.setFirstName(customer.getFirstName());
            findCustomer.setLastName(customer.getLastName());
            findCustomer.setListInvoice(customer.getListInvoice());
            findCustomer.setPhone(customer.getPhone());
            findCustomer.setUpdatedTime(customer.getUpdatedTime());
            customerRepository.save(findCustomer);
        }
        return findCustomer;
    }
}
