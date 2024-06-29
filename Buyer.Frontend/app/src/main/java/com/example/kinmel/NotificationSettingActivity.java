package com.example.kinmel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class NotificationSettingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private static final String TAG = "NotificationSetting";
    private Switch switchNotification;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationactivity);

        switchNotification = findViewById(R.id.switchNotification);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "Permission granted");
                scheduleNotification();
                Toast.makeText(NotificationSettingActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Permission denied");
                Toast.makeText(NotificationSettingActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                switchNotification.setChecked(false);
            }
        });

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d(TAG, "Switch enabled");
                // Check and request POST_NOTIFICATIONS permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    } else {
                        scheduleNotification();
                        Toast.makeText(NotificationSettingActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    scheduleNotification();
                    Toast.makeText(NotificationSettingActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "Switch disabled");
                cancelNotification();
                Toast.makeText(NotificationSettingActivity.this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleNotification() {
        Log.d(TAG, "Scheduling notification");
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set the time for 10:00 AM
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 11);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed for today, set it for tomorrow
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d(TAG, "Notification scheduled");
        } else {
            Log.d(TAG, "AlarmManager is null");
        }
    }

    private void cancelNotification() {
        Log.d(TAG, "Cancelling notification");
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Notification cancelled");
        } else {
            Log.d(TAG, "AlarmManager is null");
        }
    }
}
