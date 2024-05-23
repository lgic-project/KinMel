package com.example.kinmel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class BuyerAddtoCart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddtocart);

        // Example usage:
        addProductItem();
    }

    // Method to add a new product item dynamically
    private void addProductItem() {
        // Inflate the product item layout
        View productItemLayout = LayoutInflater.from(this).inflate(R.layout.buyeraddtocart, null);

        // Find views within the product item layout and set their details
        // Example:
        // TextView productName = productItemLayout.findViewById(R.id.productName);
        // productName.setText("New Product");

        // Get the parent layout where you want to add the new product item
        LinearLayout productContainer = findViewById(R.id.productContainer);

        // Add the new product item below the existing ones
        productContainer.addView(productItemLayout);
    }
}
