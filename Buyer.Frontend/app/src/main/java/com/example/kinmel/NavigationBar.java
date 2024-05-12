package com.example.kinmel;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class NavigationBar extends AppCompatActivity {
    ImageView btnHome,btnMessage,btnCart,btnAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        btnHome = findViewById(R.id.btnHome);
        btnMessage = findViewById(R.id.btnMessage);
        btnCart = findViewById(R.id.btnCart);
        btnAccount = findViewById(R.id.btnAccount);
        btnHome.setOnClickListener(v -> {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnMessage.setOnClickListener(v -> {
            Fragment fragment = new MessageFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnCart.setOnClickListener(v -> {
            Fragment fragment = new CartFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
        btnAccount.setOnClickListener(v -> {
            Fragment fragment = new AccountFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
        });
    }
}
