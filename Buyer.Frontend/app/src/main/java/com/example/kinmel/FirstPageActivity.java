package com.example.kinmel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.firstpageactivity);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            Log.d("SavedToken", token );
            // Token exists, navigate to HomeFragment
            Intent intent = new Intent(FirstPageActivity.this, NavigationBar.class);
            startActivity(intent);
            finish(); // To prevent the user from going back to the login screen
        }
        Button button = findViewById(R.id.signInButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, BuyerLogin.class);
            startActivity(intent);
            finish();
        });
    }
}
