package com.example.kinmel;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvName,tvName1, tvEmail, tvAddress, tvPhoneNumber;
    private ImageView ivProfilePicture;
    private String storedProfilePicture;
    private SharedPreferences sharedPreferences;
    private Boolean imageChanged =false;
    private String token;
    private Button btnUpdate;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    ivProfilePicture.setImageURI(uri);
                    selectedImageUri = uri; // set selectedImageUri to the Uri of the selected image
                    imageChanged = true;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);
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
        ivProfilePicture = findViewById(R.id.profileImage);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        Log.d("UserToken", token);

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChanged = true;
                Log.d("ImageChanged", String.valueOf(imageChanged));
                mGetContent.launch("image/*");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setTitle("Update Profile")
                        .setMessage("Are you sure you want to update?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String firstName = tvName.getText().toString().split(" ")[0];
                                Log.d("UserDetail", firstName);
                                String lastName = tvName.getText().toString().split(" ")[1];
                                Log.d("UserDetail", lastName);
                                String address = tvAddress.getText().toString();
                                Log.d("UserDetail", address);
                                Long phoneNumber = Long.parseLong(tvPhoneNumber.getText().toString());
                                Log.d("UserDetail", String.valueOf(phoneNumber));

                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("firstName", firstName);
                                    jsonBody.put("lastName", lastName);
                                    jsonBody.put("address", address);
                                    jsonBody.put("phoneNumber", phoneNumber);
                                    Log.d("ImageChangedCheck", String.valueOf(imageChanged));
                                    if (imageChanged) {
                                        try {
                                            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                                            String encodedImage = ImageUtils.encodeImageToBase64(inputStream);
                                            Log.d("EncodedImage", encodedImage);
                                            jsonBody.put("profilePhoto", encodedImage);
                                            String imageFormat = getMimeType(selectedImageUri);
                                            if (imageFormat != null && imageFormat.contains("/")) {
                                                imageFormat = imageFormat.substring(imageFormat.indexOf("/") + 1);
                                            }
                                            Log.d("ImageFormat", imageFormat);
                                            jsonBody.put("imageFormat", imageFormat);
                                        } catch (Exception e) {
                                            Log.d("ImageError", e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ApiStatic.UPDATE_USER_PROFILE, jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                tvName.clearFocus();
                                                tvName1.clearFocus();
                                                tvEmail.clearFocus();
                                                tvAddress.clearFocus();
                                                tvPhoneNumber.clearFocus();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
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

                                Volley.newRequestQueue(UserProfileActivity.this).add(jsonObjectRequest);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Snackbar.make(findViewById(R.id.updateProfileLayout), "Profile Updated", Snackbar.LENGTH_SHORT).show();
            }});

    }

    private void fetchUserData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiStatic.FETCH_USER_DETAIL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String firstName = response.getString("first_name");
                            String lastName = response.getString("last_name");
                            String fullName = firstName + " " + lastName;
                            tvName.setText(fullName);
                            tvName1.setText(fullName);
                            tvEmail.setText(response.getString("email"));
                            tvAddress.setText(response.getString("address"));
                            tvPhoneNumber.setText(response.getString("phoneNumber"));
                            String profilePicture = response.getString("profilePicture");
                            Log.d("ProfilePicture", profilePicture);
                            storedProfilePicture = profilePicture;
                            byte[] decodedString = Base64.decode(profilePicture, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            ivProfilePicture.setImageBitmap(decodedByte);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}