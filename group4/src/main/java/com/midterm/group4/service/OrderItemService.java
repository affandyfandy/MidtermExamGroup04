package com.midterm.group4.service;

import com.midterm.group4.data.model.OrderItem;

import java.util.List;
import java.util.UUID;

import com.midterm.group4.dto.OrderItemDTO;
import org.springframework.data.domain.Page;

public interface OrderItemService {
    Page<OrderItem> findAll(int pageNo, int pageSize);
    OrderItem findOrderItemById(UUID id);
    OrderItem createOrderItem(OrderItem orderItem);
    OrderItem updateOrderItem(UUID id, OrderItem orderItem);
    OrderItem createOrderItem(OrderItemDTO orderItemDTO);
    List<OrderItem> findAllByIds(List<UUID> orderItemIds);
}
