package com.midterm.group4.controller;

import com.midterm.group4.dto.OrderItemDTO;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<Page<OrderItem>> getAllOrderItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderItem> orderItems = orderItemService.getAllOrderItems(pageable);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable UUID id) {
        Optional<OrderItem> orderItem = orderItemService.getOrderItemById(id);
        return orderItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItem createdOrderItem = orderItemService.createOrderItem(orderItemDTO);
        return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable UUID id, @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItemDTO);
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable UUID id) {
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
