package com.example.kinmelsellerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    private TextView tvName,tvName1, tvEmail, tvAddress, tvPhoneNumber;
    private Button btnUpdate;
    private SharedPrefManager sharedPrefManager;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        fetchUserData();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btnUpdate = findViewById(R.id.btnUpdate);
        tvName = findViewById(R.id.tvName);
        tvName1 = findViewById(R.id.tvName1);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        token = sharedPrefManager.getToken();
        Log.d("UserToken", token);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateProfileActivity.this)
                        .setTitle("Update Profile")
                        .setMessage("Are you sure you want to update?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String firstName = tvName.getText().toString();
                                String address = tvAddress.getText().toString();
                                Log.d("UserDetail", address);
                                Long phoneNumber = Long.parseLong(tvPhoneNumber.getText().toString());
                                Log.d("UserDetail", String.valueOf(phoneNumber));

                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("firstName", firstName);
                                    jsonBody.put("address", address);
                                    jsonBody.put("phoneNumber", phoneNumber);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, AppStatic.UPDATE_USER_PROFILE, jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                tvName.clearFocus();
                                                tvName1.clearFocus();
                                                tvEmail.clearFocus();
                                                tvAddress.clearFocus();
                                                tvPhoneNumber.clearFocus();
                                                Snackbar.make(findViewById(R.id.updateProfileLayout), "Profile Updated", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(UpdateProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                                        // handle error
                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> headers = new HashMap<>();
                                        headers.put("Authorization", "Bearer " + token);
                                        return headers;
                                    }
                                };

                                Volley.newRequestQueue(UpdateProfileActivity.this).add(jsonObjectRequest);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }});





    }
    private void fetchUserData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppStatic.FETCH_USER_DETAIL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String firstName = response.getString("first_name");
                            tvName.setText(firstName);
                            tvName1.setText(firstName);
                            tvEmail.setText(response.getString("email"));
                            tvAddress.setText(response.getString("address"));
                            tvPhoneNumber.setText(response.getString("phoneNumber"));
                        } catch (JSONException e) {
                            Toast.makeText(UpdateProfileActivity.this, "Error fetching profile 0", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateProfileActivity.this, "Error fetching profile ", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
