package com.example.kinmel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import org.json.JSONException;
import org.json.JSONObject;

public class BuyerLogin extends AppCompatActivity {


    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private Button guestButton;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyerlogin);

        TextView gotoSignInPage = findViewById(R.id.sign_up);

        emailEditText = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.btn_login);
        guestButton = findViewById(R.id.guestButton);

        requestQueue = Volley.newRequestQueue(this);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
        Log.d("SavedToken", token );
            // Token exists, navigate to HomeFragment
            Intent intent = new Intent(BuyerLogin.this, NavigationBar.class);
            startActivity(intent);
            finish(); // To prevent the user from going back to the login screen
        }
        Log.d("SavedToken", "No Token Available" );


        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyerLogin.this, NavigationBar.class);
                startActivity(intent);
            }
        });

        gotoSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(BuyerLogin.this, BuyerSignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                sendLoginRequest(email, password);
            } else {
                // Handle empty fields
                showErrorMessage("Please fill in all fields");
                loginButton.setEnabled(true);
            }
        });
    }

    private void sendLoginRequest(String email, String password) {
        String url = ApiStatic.LOGIN_ACCOUNT_API + "?username=" + email + "&password=" + password;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> handleLoginResponse(response), // Success listener (using lambda expression)
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 403) {
                        Log.d("Unverified", "User not verified");
                        showErrorMessage("Something went wrong. Please try again later.");
                        loginButton.setEnabled(true);
                        // Handle user not verified (e.g., display error message, navigate to verification)
                    } else {
                        // Handle other errors (e.g., network issues, server errors)
                        String errorMessage;
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        } else {
                            errorMessage = error.getMessage();
                        }
                        showErrorMessage("Something went wrong. Please try again later.");
                        loginButton.setEnabled(true);
//                        Log.d("LoginError", errorMessage);
                        if (errorMessage != null) {
                            Log.d("LoginError", errorMessage);
                        } else {
                            Log.d("LoginError", "Error message is null");
                        }
                    }
                }); // Error listener (using lambda expression)

        requestQueue.add(request);
    }

    private void handleLoginResponse(JSONObject response) {
        try {
            Log.d("Response", response.toString());
            int statusCode = response.getInt("status");

            if (statusCode == 200) {
                String token = response.getString("token");
                                saveTokenToSharedPreferences(token);
                Log.d("Token", token);
                loginButton.setEnabled(true);
                Intent intent = new Intent(BuyerLogin.this, NavigationBar.class);
                startActivity(intent);
                // Navigate to the next activity or perform any other actions
            } else {
                Log.d("Error", "An error occurred");
                // Handle other errors
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginError(VolleyError error) {
        String errorMessage;
        if (error.networkResponse != null && error.networkResponse.data != null) {
            errorMessage = new String(error.networkResponse.data);
        } else {
            errorMessage = error.getMessage();
        }
        Log.d("LoginError", errorMessage);
        // Handle error
    }

    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}



