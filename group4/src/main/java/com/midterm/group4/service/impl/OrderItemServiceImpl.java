package com.midterm.group4.service.impl;

import com.midterm.group4.dto.OrderItemDTO;
import com.midterm.group4.service.OrderItemService;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService{
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Page<OrderItem> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderItemRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public OrderItem findOrderItemById(UUID id) {
        Optional<OrderItem> optOrderItem = orderItemRepository.findById(id);
        if (optOrderItem.isPresent()) return optOrderItem.get();
        return null;
    }

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItem orderItem) {
        BigInteger qty = BigInteger.valueOf(orderItem.getQuantity());
        BigInteger price = orderItem.getProduct().getPrice();
        BigInteger amount = price.multiply(qty);
        orderItem.setAmount(amount);
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItem(UUID id, OrderItem orderItem) {
        OrderItem findOrderItem = findOrderItemById(id);
        if (findOrderItem != null){
            findOrderItem.setAmount(orderItem.getAmount());
            findOrderItem.setUpdatedTime(orderItem.getUpdatedTime());
            findOrderItem.setQuantity(orderItem.getQuantity());
            findOrderItem.setInvoice(orderItem.getInvoice());
            orderItemRepository.save(findOrderItem);
        }
        return null;
    }

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        // Set properties from DTO
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setAmount(orderItemDTO.getAmount());
        // Save OrderItem
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public List<OrderItem> findAllByIds(List<UUID> orderItemIds) {
        return orderItemRepository.findAllById(orderItemIds);
    }

    // public Page<OrderItem> getAllOrderItems(Pageable pageable) {
    //     return orderItemRepository.findAll(pageable);
    // }

    // public Optional<OrderItem> getOrderItemById(UUID id) {
    //     return orderItemRepository.findById(id);
    // }

    // public OrderItem createOrderItem(OrderItemDTO orderItemDTO) {
    //     OrderItem orderItem = new OrderItem();
    //     Optional<Product> productOptional = productRepository.findById(orderItemDTO.getProductId());
    //     Optional<Invoice> invoiceOptional = invoiceRepository.findById(orderItemDTO.getInvoiceId());

    //     if (productOptional.isPresent() && invoiceOptional.isPresent()) {
    //         Product product = productOptional.get();
    //         Invoice invoice = invoiceOptional.get();

    //         orderItem.setProduct(product);
    //         orderItem.setInvoice(invoice);
    //         orderItem.setQuantity(orderItemDTO.getQuantity());
    //         orderItem.setAmount(orderItemDTO.getAmount());
    //         orderItem.setCreatedTime(LocalDateTime.now());

    //         return orderItemRepository.save(orderItem);
    //     } else {
    //         throw new IllegalArgumentException("Invalid product ID or invoice ID");
    //     }
    // }

    // public OrderItem updateOrderItem(UUID id, OrderItemDTO orderItemDTO) {
    //     Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
    //     if (orderItemOptional.isPresent()) {
    //         OrderItem orderItem = orderItemOptional.get();
    //         Optional<Product> productOptional = productRepository.findById(orderItemDTO.getProductId());
    //         Optional<Invoice> invoiceOptional = invoiceRepository.findById(orderItemDTO.getInvoiceId());

    //         if (productOptional.isPresent() && invoiceOptional.isPresent()) {
    //             Product product = productOptional.get();
    //             Invoice invoice = invoiceOptional.get();

    //             orderItem.setProduct(product);
    //             orderItem.setInvoice(invoice);
    //             orderItem.setQuantity(orderItemDTO.getQuantity());
    //             orderItem.setAmount(orderItemDTO.getAmount());
    //             orderItem.setUpdatedTime(LocalDateTime.now());

    //             return orderItemRepository.save(orderItem);
    //         } else {
    //             throw new IllegalArgumentException("Invalid product ID or invoice ID");
    //         }
    //     } else {
    //         throw new IllegalArgumentException("Invalid order item ID");
    //     }
    // }

    // public void deleteOrderItem(UUID id) {
    //     orderItemRepository.deleteById(id);
    // }
}
