package com.midterm.group4.controller;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.dto.CustomerMapper;
import com.midterm.group4.service.CustomerService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size) {
        Page<Customer> pageCustomer = customerService.findAll(page, size);
        List<Customer> listCustomer = pageCustomer.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toListDto(listCustomer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toDto(customer));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<CustomerDTO> customerActivation(@PathVariable UUID id){
        customerService.updateStatus(id, true);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(customer));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<CustomerDTO> customerDeactivation(@PathVariable UUID id){
        customerService.updateStatus(id, false);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer newCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(newCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDTO dto){
        Customer customer = customerMapper.toEntity(dto);
        Customer updatedCustomer = customerService.update(id, customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(updatedCustomer));
    }
}
