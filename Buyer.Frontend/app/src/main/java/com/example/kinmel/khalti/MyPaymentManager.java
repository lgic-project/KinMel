package com.example.kinmel.khalti;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPaymentManager {

    private static final String KHALTI_URL = "https://a.khalti.com/api/v2/epayment/initiate/";

    public void initiateKhaltiPayment(Context context, String name, String email, String phone, int amount, String returnUrl, String websiteUrl, VolleyCallback callback) {

        JSONObject customerInfo = new JSONObject();
        try {
            customerInfo.put("name", name);
            customerInfo.put("email", email);
            customerInfo.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject payload = new JSONObject();
        try {
            payload.put("return_url", returnUrl);
            payload.put("website_url", websiteUrl);
            payload.put("amount", amount);
            payload.put("purchase_order_id", generateUniqueOrderId());
            payload.put("purchase_order_name", "Test Product");
            payload.put("customer_info", customerInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String payloadString = payload.toString();

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, KHALTI_URL, new JSONObject(payloadString),
                    response -> {
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    },
                    error -> {
                        if (callback != null) {
                            callback.onError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", ApiStatic.KHALTI_PRIVATE_KEY);
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueOrderId() {
        return "ORDER-" + System.currentTimeMillis();
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject response);

        void onError(VolleyError error);
    }
}
