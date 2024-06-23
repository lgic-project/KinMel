package com.example.kinmel;

import static com.example.kinmel.StaticFiles.ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.OrderHistoryAdapter;
import com.example.kinmel.adapter.ProductAdapter;
import com.example.kinmel.adapter.ProductGridAdapter;
import com.example.kinmel.response.Order;
import com.example.kinmel.response.ProductResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistory extends Fragment {
    private OrderHistoryAdapter adapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.buyerorderhistory, container, false);

        // Initialize your RecyclerView and Adapter here before calling viewOrders()
        RecyclerView recyclerView = view.findViewById(R.id.order_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderHistoryAdapter(orderList, getContext());
        recyclerView.setAdapter(adapter);

        // Then call viewOrders()
        viewOrders();

        return view;
    }

    private void viewOrders() {
        // Fetch the orders from the server
        // Display the orders in the RecyclerView
        String token = getAuthToken();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiStatic.FETCH_ORDER_HISTORY, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            orderList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject orderObject = data.getJSONObject(i);
                                Order order = new Order();
                                order.setOrderId(orderObject.getInt("orderId"));
                                order.setImagePath(orderObject.getString("imagePath"));
                                order.setProductName(orderObject.getString("productName"));
                                order.setQuantity(orderObject.getString("quantity"));
                                order.setTotalPrice(orderObject.getString("totalPrice"));
                                order.setOrderStatus(orderObject.getString("orderStatus"));
                                order.setOrderedAt(orderObject.getString("orderedAt"));
                                orderList.add(order);
                                Log.d("Order", order.toString());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API_ERROR", "Error: ", error);
                Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private String getAuthToken() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        return token;
    }
}