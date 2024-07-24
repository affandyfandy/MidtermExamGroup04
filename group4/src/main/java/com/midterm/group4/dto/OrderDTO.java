package com.midterm.group4.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private int amount;
    private String invoiceId;
    private Date createdTime;
    private Date updatedTime;
}
