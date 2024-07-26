package com.midterm.group4.controller;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.dto.CustomerMapper;
import com.midterm.group4.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
@Tag(name = "Customer Controller", description = "Controller for managing customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of customers")
    })
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {
        Page<Customer> pageCustomer = customerService.findAll(page, size);
        List<Customer> listCustomer = pageCustomer.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toListDto(listCustomer));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toDto(customer));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate customer", description = "Activate a customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Customer activated successfully")
    })
    public ResponseEntity<CustomerDTO> customerActivation(@PathVariable UUID id) {
        customerService.updateStatus(id, true);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(customer));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate customer", description = "Deactivate a customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Customer deactivated successfully")
    })
    public ResponseEntity<CustomerDTO> customerDeactivation(@PathVariable UUID id) {
        customerService.updateStatus(id, false);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(customer));
    }

    @PostMapping
    @Operation(summary = "Create new customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Customer created successfully")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer newCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(newCustomer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Customer updated successfully")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer updatedCustomer = customerService.update(id, customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toDto(updatedCustomer));
    }
}
