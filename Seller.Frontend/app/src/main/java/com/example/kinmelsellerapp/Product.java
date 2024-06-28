package com.example.kinmelsellerapp;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private int imageResource; // Assuming image is a drawable resource

    public Product(String name, double price, int quantity, int imageResource) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageResource() {
        return imageResource;
    }
}