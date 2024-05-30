package com.example.kinmel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpageactivity);

        Button button = findViewById(R.id.signInButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPageActivity.this, BuyerLogin.class);
            startActivity(intent);
            finish();
        });
    }
}
