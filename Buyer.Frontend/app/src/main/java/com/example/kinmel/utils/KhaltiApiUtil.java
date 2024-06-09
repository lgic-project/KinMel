package com.example.kinmel.utils;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.response.PaymentResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KhaltiApiUtil {

    private static final String KHALTI_API_URL = "https://a.khalti.com/api/v2/epayment/initiate/";

    public static void makePaymentRequest(Context context, String return_url, String website_url, int amount, String purchase_order_id,
                                          String purchase_order_name, String customer_name, String customer_email,
                                          String customer_phone, String authorizationKey, JSONObject jsonBody, JSONObject customer_info,
                                          final PaymentResponseCallback callback) throws JSONException {
        // ... existing code ... // Use the passed JSONObject instead of creating a new one
        customer_info.put("name", customer_name);
        customer_info.put("email", customer_email);
        customer_info.put("phone", customer_phone);

        jsonBody.put("return_url", return_url);
        jsonBody.put("website_url", website_url);
        jsonBody.put("amount", amount);
        jsonBody.put("purchase_order_id", purchase_order_id);
        jsonBody.put("purchase_order_name", purchase_order_name);
        jsonBody.put("customer_info", customer_info);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, KHALTI_API_URL, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String pidx = response.getString("pidx");
                            String payment_url = response.getString("payment_url");

                            callback.onResponse(pidx, payment_url);
                            // TODO: Use pidx and payment_url
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // TODO: Handle response
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String serverResponse = new String(error.networkResponse.data);
                            Log.e("Server Response", serverResponse);
                        }
                        callback.onError(error);
                        // TODO: Handle error

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Key " + authorizationKey);
                Log.d("Authorization Key", "key " + authorizationKey);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}