package com.example.kinmelsellerapp;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.utils.ImageUtils;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.widget.KhaltiButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText companyEditText, addressEditText, phoneEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private KhaltiButton loginButton;
    private ImageView ivProfilePicture;
    private Uri selectedImageUri;
    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    ivProfilePicture.setImageURI(uri);
                    selectedImageUri = uri;
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellersignup);

         companyEditText = findViewById(R.id.company_name);
         addressEditText = findViewById(R.id.company_address);
         phoneEditText = findViewById(R.id.company_phone_number);
         emailEditText = findViewById(R.id.company_email);
         passwordEditText = findViewById(R.id.password);
         confirmPasswordEditText = findViewById(R.id.retype_password);
        TextView termsTextView = findViewById(R.id.text_view_conditions);
        termsCheckBox = findViewById(R.id.checkbox_terms);
        ivProfilePicture = findViewById(R.id.company_logo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        termsTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = "https://www.termsandconditionsgenerator.com/live.php?token=PnlY9olshEOO1HB1JbJBA7i8Qy9irpa1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });




        loginButton = findViewById(R.id.kb_buy);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String company = companyEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();


                if (!validateInputs(company,address,phone,email, password, confirmPassword)) {
                    return;
                }
                khaltiImplement(loginButton, getApplicationContext(), UUID.randomUUID().toString(), "test", AppStatic.khaltiSellerRegistrationPrice);
                // Continue with login process
            }
        });

        TextView gotoSignUpPage = findViewById(R.id.sign_in);
        gotoSignUpPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Start BuyerSignUp activity
                Intent intent = new Intent(SignUpActivity.this, SellerLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void khaltiImplement(KhaltiButton loginButton, Context applicationContext, String paymentId, String test, Long khaltiSellerRegistrationPrice) {

        Config.Builder builder = new Config.Builder(AppStatic.khaltiPublicKey, paymentId, "Seller", khaltiSellerRegistrationPrice, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.d("Khalti", data.toString());
                Integer amount = (Integer) data.get("amount");
               Log.d("Khalti", amount.toString());
                Toast.makeText(applicationContext, "Payment successful", Toast.LENGTH_SHORT).show();
                makeSignUpRequest();
            }
        });

        Config config = builder.build();
        loginButton.setCheckOutConfig(config);
        KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(applicationContext, config);
        loginButton.setOnClickListener(v -> khaltiCheckOut1.show());


    }

    private void makeSignUpRequest() {
        progressDialog.show();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("firstName", companyEditText.getText().toString());
            requestBody.put("lastName", "");
            requestBody.put("email", emailEditText.getText().toString());
            requestBody.put("password", passwordEditText.getText().toString());
            requestBody.put("address", addressEditText.getText().toString());
            requestBody.put("phoneNumber", phoneEditText.getText().toString());
            requestBody.put("role", 3);
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                String encodedImage = ImageUtils.encodeImageToBase64(inputStream);
                Log.d("EncodedImage", encodedImage);
                requestBody.put("profilePhoto", encodedImage);
                String imageFormat = getMimeType(selectedImageUri);
                if (imageFormat != null && imageFormat.contains("/")) {
                    imageFormat = imageFormat.substring(imageFormat.indexOf("/") + 1);
                }
                Log.d("ImageFormat", imageFormat);
                requestBody.put("imageFormat", imageFormat);
            } catch (Exception e) {
                Log.d("ImageError", e.getMessage());
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send the POST request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppStatic.USER_REGISTRATION_API, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject bodyObject = response.getJSONObject("body");
                            int status = bodyObject.getInt("status");
                            if (status == 200) {
                                String message = response.optString("message", "User Registration successful");
                                // Retrieve the email from the request body
                                String email = requestBody.getString("email");
                                progressDialog.dismiss();
                                // Start the OTPPage activity with the email as a parameter
                                Intent intent2 = new Intent(SignUpActivity.this, OtpVerification.class);
                                intent2.putExtra("email", email);
                                startActivity(intent2);
                                finish();
                                resetForm();
                            } else {
                               Toast.makeText(SignUpActivity.this, "User Registration failed.We will refund you shortly", Toast.LENGTH_SHORT).show();
                            Log.d("SignUpActivity1", response.toString());
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           Toast.makeText(SignUpActivity.this, "User Registration failed.We will refund you shortly", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                           Log.d("SignUpActivity2", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this, "User Registration failed", Toast.LENGTH_SHORT).show();
                        Log.d("SignUpActivity3", error.getMessage());
                        progressDialog.dismiss();
                        error.printStackTrace();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean validateInputs(String company,String address,String phone, String email, String password, String confirmPassword) {

        if (company.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedImageUri == null){
            Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!termsCheckBox.isChecked()) {
            Toast.makeText(SignUpActivity.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }



        if (!password.equals(confirmPassword.toString())) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(SignUpActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
            resetForm();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void resetForm() {
        companyEditText.setText("");
        addressEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        termsCheckBox.setChecked(false);
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
