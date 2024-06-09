package com.example.kinmel;

import static com.example.kinmel.StaticFiles.ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.Interface.OnQuantityChangeListener;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements OnQuantityChangeListener {

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

        RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Product product : productList) {
                    product.setSelected(true);
                }
                productAdapter.notifyDataSetChanged();
                updateTotalPrice();
                updateCheckoutButtonText();
            }
        });


        Button checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                int total = 0;
                ArrayList<Integer> selectedCartIds = new ArrayList<>();
                for (Product product : productList) {
                    if (product.isSelected()) {
                        count++;
                        total += product.getTotal();
                        selectedCartIds.add(product.getCartId());
                    }
                }
                checkoutButton.setText("Checkout (" + count + ")");
                Log.d("Total",  total+"");
                Intent intent = new Intent(getActivity(), BuyerAddressActivity.class);
                intent.putExtra("selectedCartIds", selectedCartIds);
                intent.putExtra("totalAmount", total);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> selectedCartIds = new ArrayList<>();
                for (Product product : productList) {
                    if (product.isSelected()) {
                        selectedCartIds.add(product.getCartId());
                    }
                }

                if (!selectedCartIds.isEmpty()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Confirmation")
                            .setMessage("Do you really want to delete these items?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    doCartDelete(selectedCartIds);
                                }})
                            .setNegativeButton("No", null).show();
                }
            }
        });

        fetchCartData(token);

        return view;
    }



    @Override
    public void updateCheckoutButtonText() {
        int count = 0;
        for (Product product : productList) {
            if (product.isSelected()) {
                count++;
            }
        }
        Button checkoutButton = getView().findViewById(R.id.checkoutButton);
        checkoutButton.setText("Checkout (" + count + ")");
    }

    @Override
    public void updateTotalPrice() {
        int total = 0;
        for (Product product : productList) {
            if (product.isSelected()) {
                total += product.getTotal();
            }
        }
        TextView totalTextView = getView().findViewById(R.id.totalTextView);
        totalTextView.setText("Total Rs." + total);
    }

    private void doCartDelete(ArrayList<Integer> selectedCartIds) {


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, ApiStatic.DELETE_ITEMS_FROM_CART(selectedCartIds),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                String data = jsonObject.getString("data");
                                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                                fetchCartData(sharedPreferences.getString("token", ""));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
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
                            Log.d("Product", productList.toString());
                            if (productList.isEmpty()) {
                                Log.d("Product", "Empty");
//                                productAdapter = null;
//                                productContainer.setAdapter(null);
                                productList.clear();
                            } else if (productAdapter == null) {
                                productAdapter = new ProductAdapter(productList,CartFragment.this);
                                productContainer.setAdapter(productAdapter);
                            } else {
                                productAdapter.notifyDataSetChanged();
                            }
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



    @Override
    public void onQuantityChange(int cartId, String changeValue) {

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, ApiStatic.CHANGE_QUANTITY_API(cartId, changeValue),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        fetchCartData(sharedPreferences.getString("token", ""));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cartId", String.valueOf(cartId));
                params.put("quantityChange", String.valueOf(changeValue));
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}