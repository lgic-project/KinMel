package com.example.kinmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.response.ProductResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchProducts();

        TextView viewAllTextView = findViewById(R.id.viewall);
        viewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchProducts() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiStatic.FETCH_PRODUCT_HOME_API, null,
                response -> {
                    try {
                        int status = response.getInt("status");
                        if (status == 200) {
                            JSONArray dataArray = response.getJSONArray("data");
                            List<ProductResponse> products = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject productObj = dataArray.getJSONObject(i);
                                ProductResponse product = parseProduct(productObj);
                                products.add(product);
                            }
                            // Pass the products list to the adapter
                            setupProductsAdapter(products);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        });
        requestQueue.add(request);

    }
    private ProductResponse parseProduct(JSONObject productObj) throws JSONException {
        ProductResponse product = new ProductResponse();
        product.setProductId(productObj.getInt("productId"));
        product.setProductName(productObj.getString("productName"));
        product.setProductDescription(productObj.getString("productDescription"));
        product.setBrand(productObj.getString("brand"));
        product.setCategoryName(productObj.getString("categoryName"));
        product.setPrice(productObj.getDouble("price"));
        product.setDiscountedPrice(productObj.getDouble("discountedPrice"));
        product.setStockQuantity(productObj.getInt("stockQuantity"));
        product.setSellerId(productObj.getInt("sellerId"));
        product.setProductStatus(productObj.getInt("productStatus"));
        product.setCreatedAt(productObj.getString("createdAt"));
        product.setUpdatedAt(productObj.getString("updatedAt"));
        product.setFeatured(productObj.getInt("featured"));

        JSONArray productImagesArray = productObj.getJSONArray("productImages");
        List<String> productImages = new ArrayList<>();
        for (int j = 0; j < productImagesArray.length(); j++) {
            productImages.add(productImagesArray.getString(j));
        }
        product.setProductImages(productImages);
        return product;
    }
    private void setupProductsAdapter(List<ProductResponse> products) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Replace with the actual ID of your RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);
    }


}
