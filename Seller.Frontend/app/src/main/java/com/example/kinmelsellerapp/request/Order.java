package com.example.kinmelsellerapp.request;

public class Order {

        private String orderId;
        private String address1;
        private String productName;
        private int quantity;
        private String personName;

        private String address;
        private String phoneNumber;
        private String imagePath;
        private int  Price;
        private String paymentStatus;

    public Order(String orderId, String address1, String productName, int quantity, String personName, String address, String phoneNumber, String imagePath, int price, String paymentStatus) {
        this.orderId = orderId;
        this.address1 = address1;
        this.productName = productName;
        this.quantity = quantity;
        this.personName = personName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.imagePath = imagePath;
        Price = price;
        this.paymentStatus = paymentStatus;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    // Add more fields as needed

        // Constructor, getters and setters
}
