package com.example.kinmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpageactivity);

        Button button = findViewById(R.id.signInButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(FirstPageActivity.this, BuyerLogin.class);
                startActivity(intent);
            }
        });
    }

}
