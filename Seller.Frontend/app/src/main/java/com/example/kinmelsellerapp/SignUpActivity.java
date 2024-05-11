package com.example.kinmelsellerapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellersignup);

        TextView gotoSignInPage = findViewById(R.id.sign_in);
        gotoSignInPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(SignUpActivity.this, SellerLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
