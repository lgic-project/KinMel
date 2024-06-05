package com.example.kinmel;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kinmel.adapter.ProductAdapter;


public class BuyerAddtoCart extends AppCompatActivity {

    private RecyclerView productContainer;
    private ProductAdapter productAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddtocart);
        productContainer = findViewById(R.id.productContainer);
        productContainer.setLayoutManager(new LinearLayoutManager(this));
    }
}