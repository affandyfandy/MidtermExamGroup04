package com.midterm.group4.controller;

import com.midterm.group4.data.model.Customer;
// import com.midterm.group4.dto.CustomerDTO;
import com.midterm.group4.dto.CustomerMapper;
import com.midterm.group4.dto.request.CreateCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerDTO;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.CustomerService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<ReadCustomerDTO>> getAllCustomers (
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size) {
        Page<Customer> pageCustomer = customerService.findAll(page, size);
        List<Customer> listCustomer = pageCustomer.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toListReadDto(listCustomer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadCustomerDTO> getCustomerById(@PathVariable UUID id) throws ObjectNotFoundException{
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.toReadDto(customer));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ReadCustomerDTO> customerActivation(@PathVariable UUID id) throws ObjectNotFoundException{
        customerService.updateStatus(id, true);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(customer));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ReadCustomerDTO> customerDeactivation(@PathVariable UUID id) throws ObjectNotFoundException{
        customerService.updateStatus(id, false);
        Customer customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(customer));
    }

    @PostMapping
    public ResponseEntity<ReadCustomerDTO> createCustomer(@RequestBody CreateCustomerDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer newCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(newCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadCustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CreateCustomerDTO dto){
        Customer customer = customerMapper.toEntity(dto);
        Customer updatedCustomer = customerService.update(id, customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerMapper.toReadDto(updatedCustomer));
    }
}
