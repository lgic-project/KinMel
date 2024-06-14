package com.example.kinmel;

import android.app.Activity;
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
import com.example.kinmel.response.PaymentResponseCallback;
import com.example.kinmel.utils.KhaltiApiUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddressactivity);


        Intent intent = getIntent();
        selectedCartIds = intent.getIntegerArrayListExtra("selectedCartIds");
        int totalAmount = intent.getIntExtra("totalAmount", 0);

        mobileNumberEditText = findViewById(R.id.mobile_number);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);

        Button btnSave = findViewById(R.id.btn_save);

        KhaltiButton kBuy = findViewById(R.id.kb_buy);
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
                showPaymentOptionsDialog();
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

        RadioGroup radioGroupPaymentOptions = dialogView.findViewById(R.id.radioGroupPaymentOptions);
        RadioButton radioCashOnDelivery = dialogView.findViewById(R.id.radioCashOnDelivery);
        RadioButton radioKhalti = dialogView.findViewById(R.id.radioKhalti);
        Button btnConfirmPayment = dialogView.findViewById(R.id.btnConfirmPayment);

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupPaymentOptions.getCheckedRadioButtonId();

                if (selectedId == radioCashOnDelivery.getId()) {
                    Toast.makeText(BuyerAddressActivity.this, "Cash on Delivery selected", Toast.LENGTH_SHORT).show();
                    // Cash on Delivery
                } else if (selectedId == radioKhalti.getId()) {
                    Toast.makeText(BuyerAddressActivity.this, "Khalti selected", Toast.LENGTH_SHORT).show();
                    // Khalti Payment
//                    khaltiImplement(btnConfirmPayment,getApplicationContext(),UUID.randomUUID().toString(), "test", 13000L);
//                    try {
//                        JSONObject jsonBody = new JSONObject();
//                        JSONObject customer_info = new JSONObject();
//                        KhaltiApiUtil.makePaymentRequest(
//                                BuyerAddressActivity.this,
//                                "https://example.com/payment/",
//                                "https://example.com/",
//                                13000,
//                                "test12",
//                                "test",
//                                "Sajit Gurung",
//                                "suman@gmail.com",
//                                "9816140639",
//                                "live_secret_key_68791341fdd94846a146f0457ff7b455",
//                                jsonBody,
//                                customer_info,
//                                new PaymentResponseCallback() {
//                                    @Override
//                                    public void onResponse(String pidx, String payment_url) {
//                                        // Use pidx and payment_url here
//                                        // For example, you can start a new activity and pass these values as extras in the intent
//                                    Log.d("Payment Response", "pidx: " + pidx + ", payment_url: " + payment_url);
////                                    khaltiPayment(pidx);
//
//                                    }
//
//                                    @Override
//                                    public void onError(VolleyError error) {
//                                        // Handle error here
//                                        Log.d("Payment Error", error.toString());
//                                    }
//                                }
//                        );
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                }

                dialog.dismiss();
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
                makeRequestPayment(nameInput, mobileNumberInput, addressInput, "Khalti", amount);
            }
        });

        Config config = builder.build();
        kBuy.setCheckOutConfig(config);
        KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(mCtx, config);
        kBuy.setOnClickListener(v -> khaltiCheckOut1.show());
    }

    private void makeRequestPayment(String name, String mobileNumber, String address, String paymentMethod, Integer amount) {
        Long amount1 = Long.valueOf(amount);
        Log.d("Payment", "Name: " + name + ", Mobile Number: " + mobileNumber + ", Address: " + address + ", Payment Method: " + paymentMethod + ", Amount: " + amount1 + ", Selected Cart Ids: " + selectedCartIds);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("phoneNumber", mobileNumber);
            jsonBody.put("address", address);
            jsonBody.put("paymentMethod", paymentMethod);
            jsonBody.put("orderTotal", amount1);
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
                                finish();
                            } else {
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

    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);
        return token;
    }

}
