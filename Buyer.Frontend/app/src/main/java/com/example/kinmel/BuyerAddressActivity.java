package com.example.kinmel;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import com.example.kinmel.response.PaymentResponseCallback;
import com.example.kinmel.utils.KhaltiApiUtil;
import com.khalti.checkout.Khalti;
import com.khalti.checkout.data.Environment;
import com.khalti.checkout.data.KhaltiPayConfig;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyerAddressActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;
    private CustomEditText mobileNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddressactivity);


       Intent intent = getIntent();
       ArrayList<Integer> selectedCartIds=intent.getIntegerArrayListExtra("selectedCartIds");
       int totalAmount=intent.getIntExtra("totalAmount",0);

        mobileNumberEditText = findViewById(R.id.mobile_number);
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
                    try {
                        JSONObject jsonBody = new JSONObject();
                        JSONObject customer_info = new JSONObject();
                        KhaltiApiUtil.makePaymentRequest(
                                BuyerAddressActivity.this,
                                "https://example.com/payment/",
                                "https://example.com/",
                                13000,
                                "test12",
                                "test",
                                "Sajit Gurung",
                                "suman@gmail.com",
                                "9816140639",
                                "live_secret_key_68791341fdd94846a146f0457ff7b455",
                                jsonBody,
                                customer_info,
                                new PaymentResponseCallback() {
                                    @Override
                                    public void onResponse(String pidx, String payment_url) {
                                        // Use pidx and payment_url here
                                        // For example, you can start a new activity and pass these values as extras in the intent
                                    Log.d("Payment Response", "pidx: " + pidx + ", payment_url: " + payment_url);
                                    khaltiPayment();
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        // Handle error here
                                        Log.d("Payment Error", error.toString());
                                    }
                                }
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                dialog.dismiss();
            }
        });
    }



    private void khaltiPayment() {
        int totalAmount = 1000;
        // replace 1000 with your actual total amount
        Uri returnUrl = Uri.parse("https://someurl.com");
        KhaltiPayConfig config = new KhaltiPayConfig(
                "<your_public_key>",
                "<your_pidx>",
                 returnUrl,
                 false,
                Environment.TEST
        );
    }
}
