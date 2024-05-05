package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyerSignUp extends AppCompatActivity {

    private static final String TAG ="BuyerSignUp" ;
    private EditText nameEditText, addressEditText, phoneEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private Button signUpButton;
    private Snackbar registrationSnackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyersignup);


        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        termsCheckBox = findViewById(R.id.checkbox_terms);
        signUpButton = findViewById(R.id.signUpButton);

        // Set click listener for the Sign Up button
        signUpButton.setOnClickListener(v -> signUp());

    }


    private void signUp() {
        System.out.println("Sign Up button clicked");
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Perform form validation
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match");
            return;
        }

        if (!termsCheckBox.isChecked()) {
            showErrorMessage("Please agree to the terms and conditions");
            return;
        }

        // Prepare the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("firstName", name.split(" ")[0]); // Assuming name is "First Last"
            requestBody.put("lastName", name.split(" ")[1]);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("address", address);
            requestBody.put("phoneNumber", phone);
            requestBody.put("role", 2); // Assuming role 2 is for buyers
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Show the "Registering User" Snackbar
        View rootView = findViewById(android.R.id.content);
        registrationSnackbar = Snackbar.make(rootView, "Registering User", Snackbar.LENGTH_INDEFINITE);
        registrationSnackbar.show();

        // Send the POST request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiStatic.USER_REGISTRATION_API, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Dismiss the "Registering User" Snackbar
                        registrationSnackbar.dismiss();

                        try {
                            int statusCode = response.getInt("statusCode");
                            if (statusCode == 200) {
//                                showSuccessMessage("User Registration successful");
                                resetForm();
                            } else {
                                showErrorMessage("User Registration failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showErrorMessage("User Registration failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Dismiss the "Registering User" Snackbar
                        registrationSnackbar.dismiss();

                        showErrorMessage("User Registration sucessful");
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetForm() {
        nameEditText.setText("");
        addressEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        termsCheckBox.setChecked(false);
    }

}
