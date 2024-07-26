package com.midterm.group4.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.OrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.service.InvoiceService;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.DocumentUtils;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DocumentUtils documentUtils;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate invoiceDate, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findByInvoiceDate(invoiceDate, pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findByMonth(month, pageable);
    }

    @Override
    @Transactional
    public Invoice findById(UUID id) {
        Optional<Invoice> optInvoice = invoiceRepository.findById(id);
        if (optInvoice.isPresent()) return optInvoice.get();
        return null;
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {
        List<OrderItem> listOrderItem = invoice.getListOrderItem();
        BigInteger totalAmount = BigInteger.valueOf(0);
        for (OrderItem order : listOrderItem){
            totalAmount.add(order.getAmount());
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setCreatedTime(LocalDateTime.now());
        invoice.setUpdatedTime(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem) {
        // Find the invoice to be updated
        Invoice findInvoice = findById(id);
        if (findInvoice == null) return null;
    
        // Check if the invoice can be edited based on the creation time
        LocalDateTime createdTime = findInvoice.getCreatedTime();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, currentTime);
        if (duration.toMinutes() > 10) {
            throw new IllegalArgumentException("Invoice can't be edited");
        }
    
        // Create a map for existing order items to facilitate quick lookups
        Map<UUID, OrderItem> currentOrderItemsMap = findInvoice.getListOrderItem().stream()
            .collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), orderItem -> orderItem));
    
        // Initialize total amount and updated order items list
        BigInteger totalAmount = BigInteger.ZERO;
        List<OrderItem> updatedOrderItems = new ArrayList<>();
    
        // Process new order items
        for (OrderItem newOrderItem : listOrderItem) {
            UUID productId = newOrderItem.getProduct().getProductId();
            Product product = productService.findById(productId);
    
            if (product == null) {
                throw new IllegalArgumentException("Product does not exist");
            }
            if (!product.isActive()) {
                throw new IllegalArgumentException("Product is not active");
            }
            if (product.getQuantity() < newOrderItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient product quantity");
            }
    
            BigInteger quantity = BigInteger.valueOf(newOrderItem.getQuantity());
            BigInteger price = product.getPrice();
            BigInteger amount = price.multiply(quantity);
    
            // Update the product quantity
            product.setQuantity(product.getQuantity() - newOrderItem.getQuantity());
            productRepository.save(product);
    
            // Check if the product is already in the invoice
            if (currentOrderItemsMap.containsKey(productId)) {
                // Update existing order item
                OrderItem existingOrderItem = currentOrderItemsMap.get(productId);
                BigInteger oldAmount = existingOrderItem.getAmount();
                existingOrderItem.setQuantity(newOrderItem.getQuantity());
                existingOrderItem.setAmount(amount);
                updatedOrderItems.add(existingOrderItem);
    
                // Adjust the total amount by subtracting the old amount and adding the new amount
                totalAmount = totalAmount.add(amount).subtract(oldAmount);
            } else {
                // Add new order item
                newOrderItem.setAmount(amount);
                newOrderItem.setInvoice(findInvoice);
                newOrderItem.setProduct(product);
                updatedOrderItems.add(newOrderItem);
    
                // Add the new amount to the total
                totalAmount = totalAmount.add(amount);
            }
        }
    
        // Identify and process removed order items
        List<OrderItem> removedOrderItems = findInvoice.getListOrderItem().stream()
            .filter(orderItem -> !listOrderItem.stream()
            .anyMatch(newOrderItem -> newOrderItem.getProduct().getProductId().equals(orderItem.getProduct().getProductId())))
            .collect(Collectors.toList());
    
        for (OrderItem removedOrderItem : removedOrderItems) {
            UUID productId = removedOrderItem.getProduct().getProductId();
            Product product = productService.findById(productId);
    
            if (product != null) {
                // Add the quantity of the removed item back to the product inventory
                product.setQuantity(product.getQuantity() + removedOrderItem.getQuantity());
                productRepository.save(product);
            }
    
            // Adjust the total amount by subtracting the removed item's amount
            totalAmount = totalAmount.subtract(removedOrderItem.getAmount());
        }
    
        // Update the invoice with new total amount and order items
        findInvoice.setTotalAmount(totalAmount);
        findInvoice.getListOrderItem().clear();
        findInvoice.getListOrderItem().addAll(updatedOrderItems);
    
        // Save updated invoice and order items
        invoiceRepository.save(findInvoice);
        orderItemRepository.saveAll(updatedOrderItems);
    
        return findInvoice;
    }

    @Override
    @Transactional
    public byte[] generateToPdf(UUID id) throws IOException {
        Invoice invoice = findById(id);
        if (invoice != null){
            byte[] pdfBytes = documentUtils.generateByteInvoice(invoice);
            return pdfBytes;
        }
        return null;
    }

    @Override
    @Transactional
    public Page<Invoice> findAllFiltered(int pageNo, int pageSize, String sortBy, String sortOrder, UUID customerId,
            String invoiceDate, String month) {

        // String findSortColumn = findSortBy(sortBy);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (customerId != null && invoiceDate != null && month != null) {
            // All filters applied
            return invoiceRepository.findAllFiltered(customerId, invoiceDate, month, pageable);
        } else if (customerId != null && invoiceDate != null) {
            // Customer and date filters applied
            return invoiceRepository.findAllByCustomerIdAndInvoiceDate(customerId, invoiceDate, pageable);
        } else if (customerId != null && month != null) {
            // Customer and month filters applied
            return invoiceRepository.findAllByCustomerIdAndMonth(customerId, month, pageable);
        } else if (invoiceDate != null && month != null) {
            // Date and month filters applied
            return invoiceRepository.findAllByInvoiceDateAndMonth(invoiceDate, month, pageable);
        } else if (customerId != null) {
            // Only customer filter applied
            return invoiceRepository.findAllByCustomerId(customerId, pageable);
        } else if (invoiceDate != null) {
            // Only date filter applied
            return invoiceRepository.findAllByInvoiceDate(invoiceDate, pageable);
        } else if (month != null) {
            // Only month filter applied
            return invoiceRepository.findAllByMonth(month, pageable);
        } else {
            // No filters applied
            return invoiceRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice, List<OrderItem> listOrderItem) {
        if (invoice.getListOrderItem() == null){
            invoice.setListOrderItem(new ArrayList<>());
        }

        List<OrderItem> newOrderItems = new ArrayList<>();

        Optional<Customer> cust = customerRepository.findById(invoice.getCustomer().getCustomerId());

        Customer customer;
        if (cust.isPresent()){
            customer = cust.get();
        }
        else {
            throw new IllegalArgumentException("Customer doesn't exist");
        }

        BigInteger totalAmount = BigInteger.ZERO;

        for (OrderItem orderItem : listOrderItem){

            Product product = productService.findById(orderItem.getProduct().getProductId());
            if (!product.isActive()) {
                throw new IllegalArgumentException("Product is not active");
            }
            else if (product.getQuantity() < orderItem.getQuantity()){
                throw new IllegalArgumentException("Insuffient product quantity");
            }

            BigInteger qty = BigInteger.valueOf(orderItem.getQuantity());
            BigInteger price = product.getPrice();
            BigInteger amount = price.multiply(qty);
            orderItem.setAmount(amount);

            orderItem.setInvoice(invoice);
            orderItem.setProduct(product);

            Integer qtyRemain = product.getQuantity() - orderItem.getQuantity();
            product.setQuantity(qtyRemain);

            newOrderItems.add(orderItem);

            totalAmount = totalAmount.add(amount);

            productRepository.save(product);
        }

        orderItemRepository.saveAll(newOrderItems);

        invoice.setListOrderItem(newOrderItems);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCustomer(customer);
        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
// <<<<<<< HEAD
//     public Page<Invoice> findAllByCustomerName(int pageNo, int pageSize, String sortBy, String sortOrder, String name) {
//         Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
//         Sort sort = Sort.by(direction,sortBy);
//         Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//         return invoiceRepository.findAllByCustomerName(name, pageable);
// =======
    public List<Invoice> findByCustomerName(String customerName) {
        return invoiceRepository.findByCustomerNameContaining(customerName);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerDay(LocalDate date) {
        return invoiceRepository.findTotalAmountByDate(date);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerMonth(int month, int year) {
        return invoiceRepository.findTotalAmountByMonth(month, year);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerYear(int year) {
        return invoiceRepository.findTotalAmountByYear(year);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getTop3ProductsByAmount() {
        return orderItemRepository.findTopProductsByAmount().stream()
                .map(data -> Map.of("name", data[0], "totalAmount", data[1]))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<String> getSoldProducts() {
        return orderItemRepository.findSoldProducts();
    }

    @Override
    @Transactional
    public Map<String, Long> getTotalQuantityPerProduct() {
        return orderItemRepository.findTotalQuantityPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> ((Number) data[1]).longValue()
                ));
    }

    @Override
    @Transactional
    public Map<String, BigInteger> getTotalAmountPerProduct() {
        return orderItemRepository.findTotalAmountPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> (BigInteger) data[1]
                ));
    }

    @Override
    @Transactional
    public List<Invoice> getInvoicesByFilter(UUID customerId, Integer month, Integer year) {
        if (customerId != null && month != null && year != null) {
            return invoiceRepository.findByCustomerAndMonthAndYear(customerId, month, year);
        } else if (customerId != null && month != null) {
            return invoiceRepository.findByCustomerAndMonth(customerId, month);
        } else if (customerId != null && year != null) {
            return invoiceRepository.findByCustomerAndYear(customerId, year);
        } else if (month != null && year != null) {
            return invoiceRepository.findByMonthAndYear(month, year);
        } else if (customerId != null) {
            return invoiceRepository.findByCustomer(customerId);
        } else if (year != null) {
            return invoiceRepository.findByYear(year);
        } else {
            return invoiceRepository.findAll();
        }
    }
}
