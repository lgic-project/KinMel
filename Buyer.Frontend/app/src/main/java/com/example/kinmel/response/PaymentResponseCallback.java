package com.example.kinmel.response;

import com.android.volley.VolleyError;

public interface PaymentResponseCallback {
    void onResponse(String pidx, String payment_url);
    void onError(VolleyError error);
}