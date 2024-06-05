package com.example.kinmel;

import android.app.Activity;

public class Product {
    private String name;
    private int price;
    private int discountedPrice;
    private String image;
    private int quantity;
    private int cartId;
    private int total;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Product(String name, int price, int discountedPrice, String image, int quantity, int cartId, int total) {
        this.name = name;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.image = image;
        this.quantity = quantity;
        this.cartId = cartId;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", cartId=" + cartId +
                ", total=" + total +
                '}';
    }
}
