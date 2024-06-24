package com.example.kinmel;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.MySingleton;
import com.example.kinmel.adapter.SearchAdapter;
import com.example.kinmel.adapter.SpaceItemDecoration;
import com.example.kinmel.response.ProductResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryProduct extends AppCompatActivity {
    private RecyclerView categoryProductContainerForGrid;
    private SearchAdapter productGridAdapter;
    private List<ProductResponse> productListForGrid = new ArrayList<>();
    private int categoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_products);
        categoryId = getIntent().getIntExtra("categoryId", -1);
        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryProductContainerForGrid = findViewById(R.id.category_product_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns in grid
        categoryProductContainerForGrid.setLayoutManager(gridLayoutManager);
        productGridAdapter = new SearchAdapter(this, productListForGrid); // Use the new list here
        categoryProductContainerForGrid.setAdapter(productGridAdapter);
        int spacingInPixels2 = getResources().getDimensionPixelSize(R.dimen.spacing); // Define your spacing size in dimens.xml
        categoryProductContainerForGrid.addItemDecoration(new SpaceItemDecoration(spacingInPixels2));

        fetchProductsForGrid();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        private void fetchProductsForGrid() {
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.getProductByCategory(categoryId), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data1 = response.getJSONArray("data");
                            for (int i = 0; i < data1.length(); i++) {
                                JSONObject productObject1 = data1.getJSONObject(i);
                                Integer productId1 = productObject1.getInt("productId");;
                                String productName1 = productObject1.getString("productName");
                                String productDescription1 = productObject1.getString("productDescription");
                                double price1 = productObject1.getDouble("price");
                                double discountedPrice1 = productObject1.getDouble("discountedPrice");
                                double averageRating = productObject1.getDouble("averageRating");
                                int ratingCount = productObject1.getInt("ratingCount");
                                JSONArray productImages1 = productObject1.getJSONArray("productImages");
                                String imageUrl1 =ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API+ productImages1.getString(0); // Assuming the first image is the main image

                                ProductResponse product = new ProductResponse(productId1,productName1,productDescription1, price1, discountedPrice1,averageRating,ratingCount, imageUrl1);
                                productListForGrid.add(product); // Add the product to the new list

                            }
                            productGridAdapter.notifyDataSetChanged(); // Notify the adapter that the list has changed
                            // After fetching the products and updating the adapter
                            if (productListForGrid.isEmpty()) {
                                categoryProductContainerForGrid.setVisibility(View.GONE);
                                TextView emptyView = findViewById(R.id.empty_view);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                categoryProductContainerForGrid.setVisibility(View.VISIBLE);
                                TextView emptyView = findViewById(R.id.empty_view);
                                emptyView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.d("SearchErrors", e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("SearchError", error.toString());
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest1);
    }


}
