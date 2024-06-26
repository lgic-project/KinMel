package com.example.kinmelsellerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordActivity extends AppCompatActivity {
    private SharedPrefManager sharedPrefManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.changepassword);
            sharedPrefManager = SharedPrefManager.getInstance(this);
            String token = sharedPrefManager.getToken();

            final TextInputEditText oldPassword = findViewById(R.id.oldPassword);
            final TextInputEditText newPassword = findViewById(R.id.newPassword);
            final TextInputEditText confirmPassword = findViewById(R.id.confirmPassword);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldPass = oldPassword.getText().toString();
                    String newPass = newPassword.getText().toString();
                    String confirmPass = confirmPassword.getText().toString();




                    if (newPass.equals(confirmPass)) {

                        if (newPass.length() < 6) {
                            Toast.makeText(UpdatePasswordActivity.this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!newPass.matches(".*\\d.*")) {
                            Toast.makeText(UpdatePasswordActivity.this, "Password should contain at least one number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Create a new request queue
                        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePasswordActivity.this);

                        // Create the JSON request body
                        JSONObject requestBody = new JSONObject();
                        try {
                            requestBody.put("oldPassword", oldPass);
                            requestBody.put("newPassword", newPass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Create the request
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.PUT,
                                AppStatic.CHANGE_PASSWORD_URL,
                                requestBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String data = response.getString("data");
                                            if (data.equals("Password Changed")) {
                                                Toast.makeText(UpdatePasswordActivity.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                                                showMessageDialog("Password Changed Successfully");
                                            } else if (data.equals("Password Don't Match")) {
                                                Toast.makeText(UpdatePasswordActivity.this, "Old password doesn't match", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle error
                                        Toast.makeText(UpdatePasswordActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                        ){
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + token);
                                return headers;
                            }
                        };


                        // Add the request to the queue
                        requestQueue.add(jsonObjectRequest);
                    } else {
                        Toast.makeText(UpdatePasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        oldPassword.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                    }
                }
            });
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case android.R.id.home:
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
        private void showMessageDialog(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePasswordActivity.this);
            builder.setTitle("Password Changed Successfully");
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = UpdatePasswordActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("sellerToken");
                    editor.apply();
                    Intent intent = new Intent(UpdatePasswordActivity.this, SellerLogin.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
