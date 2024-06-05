package com.example.kinmel;

import static com.example.kinmel.StaticFiles.ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView productContainer;
    private ProductAdapter productAdapter;
    SharedPreferences sharedPreferences;
    Button deleteButton;
    List<Product> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CartFragment", "Cart Fragment Initialized");

        View view = inflater.inflate(R.layout.buyeraddtocart, container, false);
        productContainer = view.findViewById(R.id.productContainer);
        productContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");


        deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> selectedCartIds = new ArrayList<>();
                for (Product product : productList) {
                    if (product.isSelected()) {
                        selectedCartIds.add(product.getCartId());
                    }
                }
                // Now selectedCartIds contains the cartIds of all selected products
                Log.d("Selected CartIds", selectedCartIds.toString());
            }
        });

        fetchCartData(token);

        return view;
    }

    private void fetchCartData( String token) {

        Log.d("AddToCart",token);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiStatic.FETCH_USER_CART, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            productList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject productJson = data.getJSONObject(i);
                                Product product = new Product(
                                        productJson.getString("productName"),
                                        productJson.getInt("price"),
                                        productJson.getInt("discountedPrice"),
                                        FETCH_PRODUCT_IMAGE_HOME_API+ productJson.getString("productImagePath"),
                                        productJson.getInt("quantity"),
                                        productJson.getInt("cartId"),
                                        productJson.getInt("total")
                                );
                                Log.d("PRODUCT", product.getImage());
                                Log.d("PRODUCT", product.getName());
                                Log.d("PRODUCT", product.getPrice() + "");
                                Log.d("PRODUCT", product.getDiscountedPrice() + "");
                                Log.d("PRODUCT", product.getQuantity() + "");
                                Log.d("PRODUCT", product.getCartId() + "");
                                Log.d("PRODUCT", product.getTotal() + "");

                                productList.add(product);
                            }
                            productAdapter = new ProductAdapter(productList);
                            productContainer.setAdapter(productAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }
}