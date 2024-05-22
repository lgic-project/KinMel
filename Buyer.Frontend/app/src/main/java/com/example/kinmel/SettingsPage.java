package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyersetting);

        TextView profileSettingTextView = findViewById(R.id.tvProfileSetting);
        profileSettingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, UpdateProfileActivity.class);
                startActivity(intent);

            }
        });

        TextView updatePasswordTextView = findViewById(R.id.btnUpdatePassword);
        updatePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, UpdatePasswordActivity.class);
                startActivity(intent);

            }
        });
   }
}
