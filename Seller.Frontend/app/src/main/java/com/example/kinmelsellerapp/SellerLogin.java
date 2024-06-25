package com.example.kinmelsellerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SellerLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlogin);

        TextView gotoSignUpPage = findViewById(R.id.sign_up);
        gotoSignUpPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(SellerLogin.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
