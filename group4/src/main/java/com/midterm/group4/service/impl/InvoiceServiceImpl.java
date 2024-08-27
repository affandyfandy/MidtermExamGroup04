package com.midterm.group4.service.impl;

import java.io.ByteArrayOutputStream;
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
import java.util.LinkedHashMap;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.InvoiceService;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.DocumentUtils;
import com.midterm.group4.utils.ExcelUtils;

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
    private ExcelUtils excelUtils;

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
    public Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findAllByMonth(month, pageable);
    }

    @Override
    @Transactional
    public Invoice findById(UUID id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Invoice not found with ID: " + id));
    }

    @Override
    @Transactional
    public Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem) {
        Invoice findInvoice = findById(id);
        if (findInvoice == null) new ObjectNotFoundException("Invoice not found with ID: " + id);
    
        LocalDateTime createdTime = findInvoice.getCreatedTime();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, currentTime);
        if (duration.toMinutes() > 10) {
            throw new IllegalArgumentException("Invoice can't be edited: over 10 minutes from being created");
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
                throw new ObjectNotFoundException("Product not found");
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
    
        // Update the invoice with the new total amount and list of order items
        findInvoice.setTotalAmount(totalAmount); // Assuming Invoice has a totalAmount field
        findInvoice.setListOrderItem(updatedOrderItems);
    
        // Save the updated invoice
        invoiceRepository.save(findInvoice);
    
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
            return invoiceRepository.findAllByMonth(Integer.parseInt(month), pageable);
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
            if (!customer.isActive()) throw new IllegalArgumentException("Customer is inactive");
        }
        else {
            throw new IllegalArgumentException("Customer doesn't exist");
        }

        BigInteger totalAmount = BigInteger.ZERO;

        if (listOrderItem.isEmpty()){
            throw new IllegalArgumentException("List order can't be empty");
        }

        for (OrderItem orderItem : listOrderItem){

            Product product = productService.findById(orderItem.getProduct().getProductId());
            if (product == null){
                throw new ObjectNotFoundException("Product doesn't exist");
            }
            else if (!product.isActive()) {
                throw new IllegalArgumentException("Product is inactive");
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
    public Page<Invoice> findAllByCustomerName(int pageNo, int pageSize, String sortBy, String sortOrder, String name) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findAllByCustomerName(name, pageable);
    }

    public BigInteger getTotalAmountPerDay(LocalDate date) {
        return invoiceRepository.findTotalAmountByDate(date);
    }

    public BigInteger getTotalAmountPerMonth(int month, int year) {
        return invoiceRepository.findTotalAmountByMonth(month, year);
    }

    public BigInteger getTotalAmountPerYear(int year) {
        return invoiceRepository.findTotalAmountByYear(year);
    }

    public List<Map<String, Object>> getTop3ProductsByAmount() {
        return orderItemRepository.findTopProductsByAmount().stream()
                .map(data -> Map.of("name", data[0], "totalAmount", data[1]))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<String> getSoldProducts() {
        return orderItemRepository.findSoldProducts();
    }

    public Map<String, Long> getTotalQuantityPerProduct() {
        return orderItemRepository.findTotalQuantityPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> ((Number) data[1]).longValue()
                ));
    }

    public Map<String, BigInteger> getTotalAmountPerProduct() {
        return orderItemRepository.findTotalAmountPerProduct()
            .stream().collect(Collectors.toMap(
                    data -> (String) data[0],
                    data -> (BigInteger) data[1]
            ));
    }

    public List<Invoice> generateInvoiceReportData(UUID customerId, Integer month, Integer year) {
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

    @Override
    @Transactional
    public Map<String, Object> generateReport(LocalDate date, int month, int year){
        if (date == null && month == 0 && year == 0) {
            throw new IllegalArgumentException("At least one parameter (date, month, year) is required");
        }
        BigInteger totalAmountPerDay = date != null ? getTotalAmountPerDay(date) : null;
        BigInteger totalAmountPerMonth = (month != 0 && year != 0) ? getTotalAmountPerMonth(month, year) : null;
        BigInteger totalAmountPerYear = year != 0 ? getTotalAmountPerYear(year) : null;

        List<Map<String, Object>> topProductsByAmount = getTop3ProductsByAmount();
        List<String> soldProducts = getSoldProducts();
        Map<String, Long> totalQuantityPerProduct = getTotalQuantityPerProduct();
        Map<String, BigInteger> totalAmountPerProduct = getTotalAmountPerProduct();

        Map<String, Object> report = new LinkedHashMap<>();
        if (date != null) {
            report.put("Revenue generated this day", totalAmountPerDay);
        }
        if (month != 0) {
            report.put("Revenue generated this month", totalAmountPerMonth);
        }
        if (year != 0 && month == 0) {
            report.put("Revenue generated this year", totalAmountPerYear);
        }
        report.put("Top 3 Products By Amount", topProductsByAmount);
        report.put("Product Quantity Sold", totalQuantityPerProduct);
        report.put("Revenue Per Product", totalAmountPerProduct);
        report.put("Sold Products", soldProducts);
        return report;
    }

    @Override
    @Transactional
    public ByteArrayOutputStream exportInvoiceToExcel(List<Invoice> listInvoice) throws IOException{
        return excelUtils.exportInvoice(listInvoice);
    }
}