package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BuyerSignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyersignup);

        Button signInButton = findViewById(R.id.btn_go_to_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BuyerSignIn activity
                Intent intent = new Intent(BuyerSignUp.this, BuyerSignIn.class);
                startActivity(intent);
            }
        });
    }
}
