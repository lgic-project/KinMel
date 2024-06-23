package com.example.kinmel;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.MySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductPage extends AppCompatActivity {
    private Integer productId;
    private Integer totalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productpage);
//        productId = Integer.parseInt(getIntent().getStringExtra("productId"));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        productId = getIntent().getIntExtra("productId", -1);
        // Use the productId to fetch product details
        Log.d("Product IDsss", String.valueOf(productId));
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart();
            }
        });
        Button btnBuyNow = findViewById(R.id.btnBuyNow);
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               buyItem();
            }
        });
        Button addReview= findViewById(R.id.addRatingbtn);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                // Check if the token exists in shared preferences
                String token = sharedPreferences.getString("token", "");
                if (token.isEmpty()){
                    Toast.makeText(ProductPage.this, "Login To give rating", Toast.LENGTH_SHORT).show();
                }
                rating();
            }

        });
//     fetchSlider();
     fetchProductData();
    }

    private void rating() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rating, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductPage.this);
        builder.setView(dialogView);

        // Get the RatingBar and buttons from the dialog layout
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        Button addButton = dialogView.findViewById(R.id.addButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(ProductPage.this, "Rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        // Set the OnClickListener for the "Add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                Log.d("Rating", "User rating: " + rating);
                Log.d("Rating", "Product ID: " + productId);
                if (rating==0){
                    Toast.makeText(ProductPage.this, "Rating should be least 1 star", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                JSONObject params = new JSONObject();
                try {
                    params.put("productId", productId);
                    params.put("rating", rating);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Get the token from shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Log.d("Rating", "Token: " + token);
                Log.d("Rating", "Params: " + params.toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, ApiStatic.GIVE_RATING_API, params, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int status = response.getInt("status");
                                    String statusValue = response.getString("statusValue");
                                    String data = response.getString("data");
                                    if (status==200){
                                         View view = findViewById(R.id.product_page_layout);  // Fixed line
                                        Snackbar.make(view, data, Snackbar.LENGTH_SHORT).show();
                                        fetchProductData();
                                    }
                                    else {
                                        Toast.makeText(ProductPage.this, "Data: " + data, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ProductPage.this, "Data: " + "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Toast.makeText(ProductPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                // Add the request to the RequestQueue.
                MySingleton.getInstance(ProductPage.this).addToRequestQueue(jsonObjectRequest);

                dialog.dismiss();
            }
        });
        // Set the OnClickListener for the "Cancel" button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void buyItem() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.isEmpty()){
            Intent intent = new Intent(ProductPage.this, BuyerLogin.class);
            startActivity(intent);
        }
        else{
            ArrayList<Integer> selectedCartIds = new ArrayList<>();
            selectedCartIds.add(productId);
            Intent intent = new Intent(ProductPage.this, BuyerAddressActivity.class);
            intent.putIntegerArrayListExtra("selectedCartIds", selectedCartIds);
            intent.putExtra("totalAmount", totalPrice);
            Log.d("ProductTotalPrice", String.valueOf(totalPrice));
            Log.d("ProductProductID", String.valueOf(selectedCartIds));
            startActivity(intent);

        }

    }

    private void addtocart() {
        // Get the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if the token exists in shared preferences
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            // If the token doesn't exist, navigate to BuyerLoginPage
            Intent intent = new Intent(ProductPage.this, BuyerLogin.class);
            startActivity(intent);
        } else {
            // If the token exists, send a POST request to the API

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiStatic.ADD_TO_CART_API(productId), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String data = response.getString("data");
                                if (data.contains("Added to cart")) {
                                    Toast.makeText(ProductPage.this, "Added to cart successful", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ProductPage.this, "Error when adding to cart ", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

//    private void fetchSlider() {
//        ArrayList<SlideModel> imageList = new ArrayList<>();
//        imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Big Sale", ScaleTypes.CENTER_CROP));
//        imageList.add(new SlideModel("https://bit.ly/2BteuF2",ScaleTypes.CENTER_CROP));
//        imageList.add(new SlideModel("https://bit.ly/3fLJf72",ScaleTypes.CENTER_CROP));
//
//        ImageSlider imageSlider = findViewById(R.id.product_slider);
//        imageSlider.setImageList(imageList);
//    }


    private void fetchProductData() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.getProductById(productId), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 200) {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray productImages = data.getJSONArray("productImages");

                                ArrayList<SlideModel> imageList = new ArrayList<>();
                                for (int i = 0; i < productImages.length(); i++) {
                                    String imageUrl = ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API + productImages.getString(i).trim();
                                    Log.d("ImageTag",imageUrl);
                                    imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                                }

                                ImageSlider imageSlider = findViewById(R.id.product_slider);
                                imageSlider.setImageList(imageList);
                                // Populate the TextViews with the data from the API response
                                TextView categoryName = findViewById(R.id.categoryName);
                                categoryName.setText(data.getString("categoryName"));

                                TextView brandName = findViewById(R.id.brandName);
                                brandName.setText("Brand: " + data.getString("brand"));

                                TextView productName = findViewById(R.id.productName);
                                productName.setText(data.getString("productName"));

                                TextView price = findViewById(R.id.price);
                                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                price.setText("Price: Rs." + data.getInt("price"));

                                TextView discountPrice = findViewById(R.id.discountPrice);
                                totalPrice=data.getInt("discountedPrice");
                                discountPrice.setText("Discount Price: Rs." + data.getInt("discountedPrice"));

                                TextView stock = findViewById(R.id.stock);
                                stock.setText("In Stock: " + data.getInt("stockQuantity"));

                                TextView productDescription = findViewById(R.id.productDescription);
                                productDescription.setText(data.getString("productDescription"));

                                double averageRating = data.getDouble("averageRating");
                                int ratingCount = data.getInt("ratingCount");

                                RatingBar ratingBar = findViewById(R.id.ratingBar1);
                                TextView productRating = findViewById(R.id.tvRating);
                                if (averageRating>=1){
                                ratingBar.setRating((float) averageRating);
                                }
                                else {
                                    ratingBar.setVisibility(View.GONE);
                                    productRating.setText("No ratings yet");
                                }


                                if (averageRating != 0 && ratingCount != 0) {
                                    String ratingShow = averageRating + "⭐" + "(" + ratingCount + ")";

                                    productRating.setText(ratingShow);
                                }

                            } else {
                                // handle error status
                                Toast.makeText(ProductPage.this, "Unable to show data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
                        Toast.makeText(ProductPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
