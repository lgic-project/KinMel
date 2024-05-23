package com.example.kinmel;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.MySingleton;
import com.example.kinmel.adapter.ProductAdapterMain;
import com.example.kinmel.response.ProductResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productContainer;
    private List<ProductResponse> productList = new ArrayList<>();
    private ProductAdapterMain productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        productContainer = view.findViewById(R.id.product_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productContainer.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapterMain(getActivity(), productList);
        productContainer.setAdapter(productAdapter);

        fetchProducts();

        return view;
    }

    private void fetchProducts() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.FETCH_PRODUCT_HOME_API, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject productObject = data.getJSONObject(i);
                                String productName = productObject.getString("productName");
                                Log.d("Product Name", productName);
                                String productDescription = productObject.getString("productDescription");
                                Log.d("Product Description", productDescription);
                                double price = productObject.getDouble("price");
                                Log.d("Price", String.valueOf(price));
                                double discountedPrice = productObject.getDouble("discountedPrice");
                                Log.d("Discounted Price", String.valueOf(discountedPrice));
                                JSONArray productImages = productObject.getJSONArray("productImages");
                                String imageUrl =ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API+ productImages.getString(0); // Assuming the first image is the main image

                                ProductResponse product = new ProductResponse(productName, price, discountedPrice, imageUrl);
                                productList.add(product);
                            }

                            productAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
}