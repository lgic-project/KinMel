package com.example.kinmel;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerAddressActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;
    private CustomEditText mobileNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddressactivity);

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
                }

                dialog.dismiss();
            }
        });
    }
}
