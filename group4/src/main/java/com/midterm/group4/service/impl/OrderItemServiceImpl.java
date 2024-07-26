// package com.midterm.group4.service.impl;

// import com.midterm.group4.service.InvoiceService;
// import com.midterm.group4.service.OrderItemService;
// import com.midterm.group4.service.ProductService;
// import com.midterm.group4.data.model.Invoice;
// import com.midterm.group4.data.model.OrderItem;
// import com.midterm.group4.data.model.Product;
// import com.midterm.group4.data.repository.OrderItemRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import java.math.BigInteger;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// @Service
// public class OrderItemServiceImpl implements OrderItemService{
    
//     @Autowired
//     private OrderItemRepository orderItemRepository;

//     @Autowired
//     private ProductService productService;

//     @Override
//     @Transactional
//     public Page<OrderItem> findAll(int pageNo, int pageSize) {
//         Pageable pageable = PageRequest.of(pageNo, pageSize);
//         return orderItemRepository.findAll(pageable);
//     }

//     @Override
//     @Transactional
//     public OrderItem findOrderItemById(UUID id) {
//         Optional<OrderItem> optOrderItem = orderItemRepository.findById(id);
//         if (optOrderItem.isPresent()) return optOrderItem.get();
//         return null;
//     }

//     @Override
//     @Transactional
//     public OrderItem createOrderItem(OrderItem orderItem) {
//         Product product = productService.findById(orderItem.getProduct().getProductId());
//         orderItem.setProduct(product);
//         BigInteger qty = BigInteger.valueOf(orderItem.getQuantity());
//         BigInteger price = orderItem.getProduct().getPrice();
//         BigInteger amount = price.multiply(qty);
//         orderItem.setAmount(amount);
//         orderItem.setCreatedTime(LocalDateTime.now());
//         orderItem.setUpdatedTime(LocalDateTime.now());
//         return orderItemRepository.save(orderItem);
//     }

//     @Override
//     @Transactional
//     public OrderItem updateOrderItem(UUID id, OrderItem orderItem) {
//         OrderItem findOrderItem = findOrderItemById(id);
//         if (findOrderItem != null){
//             findOrderItem.setAmount(orderItem.getAmount());
//             findOrderItem.setUpdatedTime(orderItem.getUpdatedTime());
//             findOrderItem.setQuantity(orderItem.getQuantity());
//             findOrderItem.setInvoice(orderItem.getInvoice());
//             orderItemRepository.save(findOrderItem);
//         }
//         return null;
//     }

//     @Override
//     @Transactional
//     public List<OrderItem> findAllByIds(List<UUID> orderItemIds) {
//         return orderItemRepository.findAllById(orderItemIds);
//     }

    
// }
//     @Override
//     @Transactional
//     public OrderItem createOrderItem(OrderItem orderItem) {
//         Product product = productService.findById(orderItem.getProduct().getProductId());
//         orderItem.setProduct(product);
//         BigInteger qty = BigInteger.valueOf(orderItem.getQuantity());
//         BigInteger price = orderItem.getProduct().getPrice();
//         BigInteger amount = price.multiply(qty);
//         orderItem.setAmount(amount);
//         orderItem.setCreatedTime(LocalDateTime.now());
//         orderItem.setUpdatedTime(LocalDateTime.now());
//         return orderItemRepository.save(orderItem);
//     }

//     @Override
//     @Transactional
//     public OrderItem updateOrderItem(UUID id, OrderItem orderItem) {
//         OrderItem findOrderItem = findOrderItemById(id);
//         if (findOrderItem != null){
//             findOrderItem.setAmount(orderItem.getAmount());
//             findOrderItem.setUpdatedTime(orderItem.getUpdatedTime());
//             findOrderItem.setQuantity(orderItem.getQuantity());
//             findOrderItem.setInvoice(orderItem.getInvoice());
//             orderItemRepository.save(findOrderItem);
//         }
//         return null;
//     }
// }
// >>>>>>> 9a11b7a9284ca1a9d9f08cc49ec00b001425cf48
