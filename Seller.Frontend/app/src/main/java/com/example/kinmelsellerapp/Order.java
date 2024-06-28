package com.example.kinmelsellerapp.utils;

public class Order {
    private String id;
    private String customerName;
    private String orderDate;

    public Order(String id, String customerName, String orderDate) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
