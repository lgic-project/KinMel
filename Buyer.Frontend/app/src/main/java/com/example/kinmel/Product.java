package com.example.kinmel;

import android.app.Activity;

public class Product {
    private String name;
    private String price;
    private int imageResourceId;

    public Product(String name, String price, int imageResourceId) {
        this.name = name;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
