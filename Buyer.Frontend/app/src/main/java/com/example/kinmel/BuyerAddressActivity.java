package com.example.kinmel;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyeraddressactivity);

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentOptionsDialog();
            }
        });
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
                    //Cash on Delivery
                } else if (selectedId == radioKhalti.getId()) {
                    Toast.makeText(BuyerAddressActivity.this, "Khalti selected", Toast.LENGTH_SHORT).show();
                    // Khalti Payment
                }

                dialog.dismiss();
            }
        });
    }
}


