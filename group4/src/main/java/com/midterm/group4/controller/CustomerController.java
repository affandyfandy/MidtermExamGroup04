package com.midterm.group4.controller;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.CustomerMapper;
import com.midterm.group4.dto.request.CreateCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerDTO;
import com.midterm.group4.dto.response.ReadProductDTO;
import com.midterm.group4.exception.InvalidInputException;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<Page<ReadCustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {
        Page<Customer> pageCustomer = customerService.findAll(page, size);
        List<ReadCustomerDTO> listCustomers = pageCustomer.getContent().stream()
                .map(customer -> customerMapper.toReadDto(customer))
                .collect(Collectors.toList());
        Page<ReadCustomerDTO> pageCustomerDTO = new PageImpl<>(listCustomers, pageCustomer.getPageable(), pageCustomer.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(pageCustomerDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ReadCustomerDTO> getCustomerById(@PathVariable UUID id) throws ObjectNotFoundException {
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toReadDto(customer));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate customer", description = "Activate a customer by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Customer activated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ReadCustomerDTO> customerActivation(@PathVariable UUID id) throws ObjectNotFoundException {
        customerService.updateStatus(id, true);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(customer));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate customer", description = "Deactivate a customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Customer deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ReadCustomerDTO> customerDeactivation(@PathVariable UUID id) throws ObjectNotFoundException {
        customerService.updateStatus(id, false);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(customer));
    }

    @PostMapping
    @Operation(summary = "Create new customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReadCustomerDTO> createCustomer(@Parameter(description = "Customer data to create") @RequestBody CreateCustomerDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer newCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerMapper.toReadDto(newCustomer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Customer activated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ReadCustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CreateCustomerDTO dto) throws ObjectNotFoundException {
        Customer customer = customerMapper.toEntity(dto);
        Customer updatedCustomer = customerService.update(id, customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(updatedCustomer));
    }
}
