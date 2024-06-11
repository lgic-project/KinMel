package com.example.kinmel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThankYouActivity extends AppCompatActivity {
    private Button homeButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyouactivity);
        homeButton = findViewById(R.id.continueShoppingButton);
        homeButton.setOnClickListener(v -> {
            Intent intent=new Intent(ThankYouActivity.this,NavigationBar.class);
            startActivity(intent);
            finish();
        });

    }
}
