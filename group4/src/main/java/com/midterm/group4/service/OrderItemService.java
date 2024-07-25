package com.midterm.group4.service;

import com.midterm.group4.data.model.OrderItem;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface OrderItemService {
    Page<OrderItem> findAll(int pageNo, int pageSize);
    OrderItem findOrderItemById(UUID id);
    OrderItem createOrderItem(OrderItem orderItem);
    OrderItem updateOrderItem(UUID id, OrderItem orderItem);
}
