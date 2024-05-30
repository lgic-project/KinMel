package com.example.kinmel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        CardView cardContactUs = findViewById(R.id.cardContactUs);
        CardView cardEmailUs = findViewById(R.id.cardEmailUs);
        CardView cardFAQ = findViewById(R.id.cardFAQ);
        CardView cardFeedback = findViewById(R.id.cardFeedback);

        cardContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contact us action
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+9779846789177")); // Replace with actual contact number
                startActivity(intent);
            }
        });

        cardEmailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // email us action
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "Contact us","navinamogar888@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        cardFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAQ action
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.navinabudhathoki.com.np"));
                startActivity(browserIntent);
            }
        });

        cardFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //feedback action
                Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.devkotasuman.com.np"));
                startActivity(feedbackIntent);
            }
        });
    }
}
