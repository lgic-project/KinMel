package com.example.kinmel;
import com.example.kinmel.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load the fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Apply animation to the logo and tagline
        ImageView splashLogo = findViewById(R.id.splash_logo);
        TextView splashTagline = findViewById(R.id.splash_tagline);
        splashLogo.startAnimation(fadeIn);
        splashTagline.startAnimation(fadeIn);

        // Handler to start the next activity after the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, BuyerLogin.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
