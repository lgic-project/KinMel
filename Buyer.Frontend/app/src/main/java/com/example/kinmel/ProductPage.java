package com.example.kinmel;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductPage extends AppCompatActivity {
    private Integer productId;
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
//     fetchSlider();
     fetchProductData();
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
                                discountPrice.setText("Discount Price: Rs." + data.getInt("discountedPrice"));

                                TextView stock = findViewById(R.id.stock);
                                stock.setText("In Stock: " + data.getInt("stockQuantity"));

                                TextView productDescription = findViewById(R.id.productDescription);
                                productDescription.setText(data.getString("productDescription"));
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
