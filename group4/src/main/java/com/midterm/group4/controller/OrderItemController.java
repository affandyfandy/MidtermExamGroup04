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

@RestController
@RequestMapping("/api/v1/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired ProductService productService;

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {
        Page<OrderItem> orderItems = orderItemService.findAll(page, size);
        return new ResponseEntity<>(orderItemMapper.toListDto(orderItems.getContent()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable UUID id) {
        OrderItem orderItem = orderItemService.findOrderItemById(id);
        if (orderItem != null) return new ResponseEntity<>(orderItemMapper.toDto(orderItem), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItem newOrderItem = orderItemMapper.toEntity(orderItemDTO);
        OrderItem createdOrderItem = orderItemService.createOrderItem(newOrderItem);
        return new ResponseEntity<>(orderItemMapper.toDto(createdOrderItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable UUID id, @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItem);
            return new ResponseEntity<>(orderItemMapper.toDto(updatedOrderItem), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
