package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;

public class OTPPage extends Activity {


    private EditText editTextOtp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpcode);

        // Retrieve the email from the Intent extras
        email = getIntent().getStringExtra("email");

        // Initialize views
        editTextOtp = findViewById(R.id.editTextOtp);
        TextView resendOtpTextView = findViewById(R.id.textViewResend);
        Button verifyButton = findViewById(R.id.buttonVerify);

        // Set click listener for "Resend OTP" TextView
        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(email);
            }
        });

        // Set click listener for "Verify" button
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOtp.getText().toString().trim();
                verifyAccount(email, otp);
            }
        });
    }

    private void resendOTP(String email) {
        // Create a StringRequest for the PUT request
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, ApiStatic.REGENERATE_OTP_API + "?email=" + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response
                        if (response.equalsIgnoreCase("New OTP Send Successfully")) {
                            showMessage("OTP resent successfully");

                        } else {
                            showMessage("Failed to resend OTP: " + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        showMessage("Failed to resend OTP: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verifyAccount(String email, String otp) {
        // Disable the "Verify" button
        Button verifyButton = findViewById(R.id.buttonVerify);
        verifyButton.setEnabled(false);

        // Create a StringRequest for the PUT request
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, ApiStatic.VERIFY_ACCOUNT_API + "?email=" + email + "&otp=" + otp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Re-enable the "Verify" button
                        verifyButton.setEnabled(true);

                        if (response.equalsIgnoreCase("OTP verified.Now you can login")) {
                            showMessage("Account verified successfully");
                            Intent intent = new Intent(OTPPage.this, BuyerLogin.class);
                            startActivity(intent);
                            finish();
                            // Redirect to the next activity or perform any other actions
                        } else if (response.equalsIgnoreCase("Please regenerate otp and try again")) {
                            showMessage("OTP Timeout. Please regenerate OTP and try again");
                        } else {
                            showMessage("Failed to verify account: " + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Re-enable the "Verify" button
                        verifyButton.setEnabled(true);

                        // Handle the error response
                        showMessage("Failed to verify account: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}