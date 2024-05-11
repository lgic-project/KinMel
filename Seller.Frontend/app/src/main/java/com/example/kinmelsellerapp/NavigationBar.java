package com.example.kinmelsellerapp;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class NavigationBar extends AppCompatActivity {
    ImageView btnAddProduct,btnAddCategory,btnListProduct,btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Add_Product()).commit();

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnListProduct = findViewById(R.id.btnListProduct);
        btnSetting = findViewById(R.id.btnSetting);
        btnAddProduct.setOnClickListener(v -> {
            Fragment fragment = new Add_Product();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnAddCategory.setOnClickListener(v -> {
            Fragment fragment = new RequestCategory();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnListProduct.setOnClickListener(v -> {
            Fragment fragment = new ProductList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnSetting.setOnClickListener(v -> {
            Fragment fragment = new SettingsPage();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
    }
}
