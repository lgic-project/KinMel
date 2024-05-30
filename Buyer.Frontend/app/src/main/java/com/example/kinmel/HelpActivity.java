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
                // Implement contact us action
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+1234567890")); // Replace with actual contact number
                startActivity(intent);
            }
        });

        cardEmailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement email us action
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "Contact us","navinamogar888@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        cardFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement FAQ action
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.navinabudhathoki.com.np"));
                startActivity(browserIntent);
            }
        });

        cardFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement feedback action
                Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.navinabudhathoki.com.np/feedback"));
                startActivity(feedbackIntent);
            }
        });
    }
}
