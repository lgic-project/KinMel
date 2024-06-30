package com.example.kinmelsellerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.example.kinmelsellerapp.adapter.OrdersAdapter;
import com.example.kinmelsellerapp.request.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentProductList extends Fragment {
   private List<Order> orders;
   private OrdersAdapter ordersAdapter;
    private SharedPrefManager sharedPrefManager;
    private String token;
    private  TextView empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_product, container, false);
        RecyclerView rvOrders = view.findViewById(R.id.orderContainer);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        sharedPrefManager = SharedPrefManager.getInstance(getActivity().getApplicationContext());
         empty_view= view.findViewById(R.id.empty_view);
        token = sharedPrefManager.getToken();
       orders = new ArrayList<>();
       ordersAdapter = new OrdersAdapter(orders);
        rvOrders.setAdapter(ordersAdapter);
       fetchOrders();
        return view;
    }

    private void fetchOrders() {

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Create a GET request
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, AppStatic.FETCH_DELIVERY_ORDERS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Clear the existing orders
                            JSONArray data = response.getJSONArray("data");
                            orders.clear();

                            // Loop through the array elements
                            for(int i=0; i<data.length(); i++){
                                // Get current json object
                                JSONObject orderObject = data.getJSONObject(i);

                                // Create a new Order object and add it to the orders list
                                Order order = new Order();
                                order.setOrderId(orderObject.getInt("eachItemOrderId"));
                                order.setGroupOrderId(orderObject.getInt("groupOrderId"));
                                order.setProductName(orderObject.getString("productName"));
                                order.setQuantity(orderObject.getInt("quantity"));
                                order.setPrice((int)orderObject.getDouble("totalPrice"));
                                order.setPersonName(orderObject.getString("orderPersonName"));
                                order.setPersonAddress(orderObject.getString("orderPersonAddress"));
                                order.setPersonPhoneNumber(orderObject.getString("orderPersonPhoneNumber"));
                                order.setPaymentMethod(orderObject.getString("paymentMethod"));
                                order.setImagePath(orderObject.getString("imagePath"));

                                orders.add(order);
                            }

                            // Notify the adapter that the data set has changed
                            ordersAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Display a simple toast message

                      empty_view.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity().getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the request queue
        queue.add(jsonObjectRequest);
    }

}