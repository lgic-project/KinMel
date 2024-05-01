package com.example.kinmel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BuyerSignUp extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private Button signUpButton;
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

//        Button signInButton = findViewById(R.id.btn_go_to_sign_in);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start BuyerSignIn activity
//                Intent intent = new Intent(BuyerSignUp.this, BuyerSignIn.class);
//                startActivity(intent);
//            }
//        });
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

        // Perform sign up logic here
        // ...
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();

        // Reset form fields
        resetForm();
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
