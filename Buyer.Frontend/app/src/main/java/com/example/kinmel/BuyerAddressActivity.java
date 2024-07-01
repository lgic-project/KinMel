package com.example.kinmel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.widget.KhaltiButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuyerAddressActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;
    private CustomEditText mobileNumberEditText;
    private EditText name, address;
    private ArrayList<Integer> selectedCartIds;
    Integer totalAmount;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddressactivity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        Intent intent = getIntent();
        selectedCartIds = intent.getIntegerArrayListExtra("selectedCartIds");
         totalAmount = intent.getIntExtra("totalAmount", 0);
         Log.d("Selected Cart Ids", selectedCartIds.toString());
            Log.d("Total Amount", totalAmount.toString());

        mobileNumberEditText = findViewById(R.id.mobile_number);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        fetchUserData();
        Button btnSave = findViewById(R.id.btn_save);
        mobileNumberEditText.setOnDrawableClickListener(new CustomEditText.OnDrawableClickListener() {
            @Override
            public void onDrawableClick() {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameInput = name.getText().toString().trim();
                String addressInput = address.getText().toString().trim();
                String mobileNumberInput = mobileNumberEditText.getText().toString().trim();

                if (!nameInput.isEmpty() || !addressInput.isEmpty() || !mobileNumberInput.isEmpty()) {
                    showPaymentOptionsDialog();
                } else {
                    Toast.makeText(BuyerAddressActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK && data != null) {
            Uri contactData = data.getData();
            Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = cursor.getString(phoneIndex);
                phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");
                Log.d("Phone Number", phoneNumber);
                mobileNumberEditText.setText(phoneNumber);
                cursor.close();
            }
        }
    }

    private void showPaymentOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.payment_options_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

       Button cashOnDelivery = dialogView.findViewById(R.id.buttonCashOnDelivery);
        KhaltiButton kBuy = dialogView.findViewById(R.id.kb_buy);

        kBuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nameInput = name.getText().toString().trim();
                String addressInput = address.getText().toString().trim();
                String mobileNumberInput = mobileNumberEditText.getText().toString().trim();

                if (!nameInput.isEmpty() || !addressInput.isEmpty() || !mobileNumberInput.isEmpty()) {
                    khaltiImplement(kBuy, getApplicationContext(), UUID.randomUUID().toString(), "test", Long.valueOf(totalAmount));
                } else {
                    Toast.makeText(BuyerAddressActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = name.getText().toString().trim();
                String addressInput = address.getText().toString().trim();
                String mobileNumberInput = mobileNumberEditText.getText().toString().trim();

                if (!nameInput.isEmpty() || !addressInput.isEmpty() || !mobileNumberInput.isEmpty()) {
                    makeRequestPayment(nameInput, mobileNumberInput, addressInput, "COD", totalAmount);
                } else {
                    Toast.makeText(BuyerAddressActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void khaltiImplement(KhaltiButton kBuy, Context mCtx, String productId, String productName, Long price) {
        Long priceInPaisa = price * 100;

        Config.Builder builder = new Config.Builder("test_public_key_7c837e9b57b94f6284ad8cd3367cf697", productId, productName, priceInPaisa, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.d("Khalti", data.toString());
                Integer amount = (Integer) data.get("amount");
                String nameInput = name.getText().toString().trim();
                String addressInput = address.getText().toString().trim();
                String mobileNumberInput = mobileNumberEditText.getText().toString().trim();
                makeRequestPayment(nameInput, mobileNumberInput, addressInput, "Khalti", (amount/100));
            }
        });

        Config config = builder.build();
        kBuy.setCheckOutConfig(config);
        KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(mCtx, config);
        kBuy.setOnClickListener(v -> khaltiCheckOut1.show());
    }

    private void makeRequestPayment(String name, String mobileNumber, String address, String paymentMethod, Integer amount) {
       progressDialog.show();
        Long amount1 = Long.valueOf(amount);
        Log.d("Payment", "Name: " + name + ", Mobile Number: " + mobileNumber + ", Address: " + address + ", Payment Method: " + paymentMethod + ", Amount: " + amount1 + ", Selected Cart Ids: " + selectedCartIds);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("phoneNumber", mobileNumber);
            jsonBody.put("address", address);
            jsonBody.put("paymentMethod", paymentMethod);
            jsonBody.put("orderTotal", amount1)
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the authorization token from the stored procedure
        String token = getAuthToken();

        // Create a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a new JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiStatic.PLACE_ORDER_API(selectedCartIds), jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            int status = response.getInt("status");
                            String data = response.getString("data");

                            if (status == 200 && "Order Placed".equals(data)) {
                                // Request is successful
                                Log.d("VolleyResponse", "Order placed successfully");
                                Intent intent = new Intent(BuyerAddressActivity.this, ThankYouActivity.class); // Use mCtx instead of getApplicationContext()
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Intent intent = new Intent(BuyerAddressActivity.this, HomeFragment.class); // Use mCtx instead of getApplicationContext()
                                startActivity(intent);
                                finish();
                                // Request is considered a failure
                                Log.e("VolleyResponse", "Order placement failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e("VolleyResponse", "Error: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
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
                            name.setText(fullName);
                            address.setText(response.getString("address"));
                            mobileNumberEditText.setText(response.getString("phoneNumber"));

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
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }





    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);
        return token;
    }




}
