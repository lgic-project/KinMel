package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyersetting);

        // Find the profile settings button and set an OnClickListener
        Button profileSettingButton = findViewById(R.id.tvProfileSetting);
        profileSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        // Find the update password button and set an OnClickListener
        Button updatePasswordButton = findViewById(R.id.btnUpdatePassword);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
