package com.midterm.group4.service;

import com.midterm.group4.dto.OrderItemDTO;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.OrderItemRepository;
import com.midterm.group4.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Page<OrderItem> getAllOrderItems(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    public Optional<OrderItem> getOrderItemById(UUID id) {
        return orderItemRepository.findById(id);
    }

    public OrderItem createOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        Optional<Product> productOptional = productRepository.findById(orderItemDTO.getProductId());
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(orderItemDTO.getInvoiceId());

        if (productOptional.isPresent() && invoiceOptional.isPresent()) {
            Product product = productOptional.get();
            Invoice invoice = invoiceOptional.get();

            orderItem.setProduct(product);
            orderItem.setInvoice(invoice);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setAmount(orderItemDTO.getAmount());
            orderItem.setCreatedTime(LocalDateTime.now());

            return orderItemRepository.save(orderItem);
        } else {
            throw new IllegalArgumentException("Invalid product ID or invoice ID");
        }
    }

    public OrderItem updateOrderItem(UUID id, OrderItemDTO orderItemDTO) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            Optional<Product> productOptional = productRepository.findById(orderItemDTO.getProductId());
            Optional<Invoice> invoiceOptional = invoiceRepository.findById(orderItemDTO.getInvoiceId());

            if (productOptional.isPresent() && invoiceOptional.isPresent()) {
                Product product = productOptional.get();
                Invoice invoice = invoiceOptional.get();

                orderItem.setProduct(product);
                orderItem.setInvoice(invoice);
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItem.setAmount(orderItemDTO.getAmount());
                orderItem.setUpdatedTime(LocalDateTime.now());

                return orderItemRepository.save(orderItem);
            } else {
                throw new IllegalArgumentException("Invalid product ID or invoice ID");
            }
        } else {
            throw new IllegalArgumentException("Invalid order item ID");
        }
    }

    public void deleteOrderItem(UUID id) {
        orderItemRepository.deleteById(id);
    }
}
