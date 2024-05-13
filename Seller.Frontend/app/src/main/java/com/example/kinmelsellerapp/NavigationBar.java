package com.example.kinmelsellerapp;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationBar extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bar);

        bottomNavigationView = findViewById(R.id.buttomNavView);
        frameLayout = findViewById(R.id.framelayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navAddProduct) {
                    loadFragment(new FragmentAdd_Product(), false);

                } else if (itemId == R.id.navAddCategory) {
                    loadFragment(new FragmentRequestCategory(), false);

                } else if (itemId == R.id.navListProduct) {
                    loadFragment(new FragmentProductList(), false);

                } else { //nav Profile
                    loadFragment(new FragmentSettingsPage(), false);

                }

                return true;
            }
        });

        loadFragment(new FragmentAdd_Product(), true);

    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.framelayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.framelayout, fragment);
        }


        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();

    }
}