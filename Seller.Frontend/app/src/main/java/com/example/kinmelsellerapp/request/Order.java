package com.example.kinmelsellerapp.request;

public class Order {

        private int orderId;
        private int groupOrderId;
        private String productName;
        private int quantity;
        private int  price;
        private String personName;
        private String personAddress;
        private String personPhoneNumber;
        private String paymentMethod;
        private String imagePath;

    public Order() {
    }

    public Order(int orderId, int groupOrderId, String productName, int quantity, int price, String personName, String personAddress, String personPhoneNumber, String paymentMethod, String imagePath) {
        this.orderId = orderId;
        this.groupOrderId = groupOrderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.personName = personName;
        this.personAddress = personAddress;
        this.personPhoneNumber = personPhoneNumber;
        this.paymentMethod = paymentMethod;
        this.imagePath = imagePath;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGroupOrderId() {
        return groupOrderId;
    }

    public void setGroupOrderId(int groupOrderId) {
        this.groupOrderId = groupOrderId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonPhoneNumber() {
        return personPhoneNumber;
    }

    public void setPersonPhoneNumber(String personPhoneNumber) {
        this.personPhoneNumber = personPhoneNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
