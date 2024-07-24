package com.midterm.group4.service;

import com.midterm.group4.dto.OrderDTO;
import com.midterm.group4.model.Order;
import com.midterm.group4.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomerId(orderDTO.getCustomerId());
        order.setProductId(orderDTO.getProductId());
        order.setQuantity(orderDTO.getQuantity());
        order.setAmount(orderDTO.getAmount());
        order.setInvoiceId(orderDTO.getInvoiceId());
        order.setCreatedTime(orderDTO.getCreatedTime());
        order.setUpdatedTime(orderDTO.getUpdatedTime());
        orderRepository.save(order);
        return orderDTO;
    }

    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setCustomerId(orderDTO.getCustomerId());
            order.setProductId(orderDTO.getProductId());
            order.setQuantity(orderDTO.getQuantity());
            order.setAmount(orderDTO.getAmount());
            order.setInvoiceId(orderDTO.getInvoiceId());
            order.setCreatedTime(orderDTO.getCreatedTime());
            order.setUpdatedTime(orderDTO.getUpdatedTime());
            orderRepository.save(order);
            return orderDTO;
        } else {
            return null;
        }
    }

    public boolean deleteOrder(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            orderRepository.delete(orderOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setCustomerId(order.getCustomerId());
            orderDTO.setProductId(order.getProductId());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setInvoiceId(order.getInvoiceId());
            orderDTO.setCreatedTime(order.getCreatedTime());
            orderDTO.setUpdatedTime(order.getUpdatedTime());
            return orderDTO;
        }).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setCustomerId(order.getCustomerId());
            orderDTO.setProductId(order.getProductId());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setInvoiceId(order.getInvoiceId());
            orderDTO.setCreatedTime(order.getCreatedTime());
            orderDTO.setUpdatedTime(order.getUpdatedTime());
            return orderDTO;
        } else {
            return null;
        }
    }
}
