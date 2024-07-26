package com.midterm.group4.controller;

import com.midterm.group4.dto.OrderItemDTO;
import com.midterm.group4.dto.OrderItemMapper;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.service.OrderItemService;
import com.midterm.group4.service.ProductService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/order-item")
@Tag(name = "Order Item Management", description = "APIs for managing order items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all order items", description = "Retrieve all order items with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of order items")
    })
    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10", required = false) int size) {
        Page<OrderItem> orderItems = orderItemService.findAll(page, size);
        return new ResponseEntity<>(orderItemMapper.toListDto(orderItems.getContent()), HttpStatus.OK);
    }

    @Operation(summary = "Get order item by ID", description = "Retrieve an order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of the order item"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@Parameter(description = "Order Item ID") @PathVariable UUID id) {
        OrderItem orderItem = orderItemService.findOrderItemById(id);
        if (orderItem != null) return new ResponseEntity<>(orderItemMapper.toDto(orderItem), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create new order item", description = "Create a new order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully")
    })
    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@Parameter(description = "Order Item DTO") @RequestBody OrderItemDTO orderItemDTO) {
        OrderItem newOrderItem = orderItemMapper.toEntity(orderItemDTO);
        OrderItem createdOrderItem = orderItemService.createOrderItem(newOrderItem);
        return new ResponseEntity<>(orderItemMapper.toDto(createdOrderItem), HttpStatus.CREATED);
    }

    @Operation(summary = "Update order item", description = "Update an existing order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@Parameter(description = "Order Item ID") @PathVariable UUID id, @Parameter(description = "Order Item DTO") @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItem);
            return new ResponseEntity<>(orderItemMapper.toDto(updatedOrderItem), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
