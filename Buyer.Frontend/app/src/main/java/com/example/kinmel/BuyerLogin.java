package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

public class BuyerLogin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyerlogin);

        TextView gotoSignInPage = findViewById(R.id.sign_up);
        gotoSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(BuyerLogin.this, BuyerSignUp.class);
                startActivity(intent);
            }
        });
    }
}



