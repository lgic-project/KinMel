package com.example.kinmelsellerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SellerLogin extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private RequestQueue requestQueue;
    private CheckBox rememberMeCheckBox;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlogin);

        TextView gotoSignUpPage = findViewById(R.id.sign_up);
        emailEditText = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.btn_login);
        rememberMeCheckBox = findViewById(R.id.rememberMe);
        requestQueue = Volley.newRequestQueue(this);

       sharedPrefManager = SharedPrefManager.getInstance(this);
        String token = sharedPrefManager.getToken();
        if (token != null) {
            Log.d("SavedToken", token );
            // Token exists, navigate to HomeFragment
            Intent intent = new Intent(SellerLogin.this, NavigationBar.class);
            startActivity(intent);
            finish(); // To prevent the user from going back to the login screen
        }
        Log.d("SavedToken", "No Token Available" );
        gotoSignUpPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(SellerLogin.this, SignUpActivity.class);
                startActivity(intent);
                finish();
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
        String url = AppStatic.LOGIN_ACCOUNT_API + "?username=" + email + "&password=" + password;

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
                sharedPrefManager.saveToken(token);
                Log.d("Token", token);
                loginButton.setEnabled(true);
                Intent intent = new Intent(SellerLogin.this, NavigationBar.class);
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
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
