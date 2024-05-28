package com.example.kinmel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class SettingsPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SettingsPage", "onCreate called");
        setContentView(R.layout.buyersetting);
        TextView tvProfileSetting = findViewById(R.id.tvProfileSetting);


        tvProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("SettingsPage", "Profile setting clicked");
//                Intent intent = new Intent(SettingsPage.this, UserProfileActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void showMessageDialog(String title, String message) {

    }
}