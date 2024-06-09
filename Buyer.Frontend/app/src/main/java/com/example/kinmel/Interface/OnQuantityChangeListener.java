package com.example.kinmel.Interface;

public interface OnQuantityChangeListener {
    void onQuantityChange(int cartId, String changeValue);
    void updateTotalPrice();
    void updateCheckoutButtonText();
}