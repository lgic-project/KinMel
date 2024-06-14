package com.example.kinmel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView productContainerForGrid;

    private DrawerLayout drawerLayout;
    private SearchAdapter productGridAdapter;
    private List<ProductResponse> productListForGrid = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");


        EditText searchBar = findViewById(R.id.search_box13);

        // Remove focus from the search bar
        searchBar.clearFocus();

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);

        productContainerForGrid = findViewById(R.id.product_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns in grid
        productContainerForGrid.setLayoutManager(gridLayoutManager);
        productGridAdapter = new SearchAdapter(this, productListForGrid); // Use the new list here
        productContainerForGrid.setAdapter(productGridAdapter);
        int spacingInPixels2 = getResources().getDimensionPixelSize(R.dimen.spacing); // Define your spacing size in dimens.xml
        productContainerForGrid.addItemDecoration(new SpaceItemDecoration(spacingInPixels2));

        fetchProductsForGrid(query);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView filterIcon = findViewById(R.id.filter_icon);

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });


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
    private void fetchProductsForGrid(String query) {
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.FETCH_SEARCH_API(query), null, new Response.Listener<JSONObject>() {

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
                                JSONArray productImages1 = productObject1.getJSONArray("productImages");
                                String imageUrl1 =ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API+ productImages1.getString(0); // Assuming the first image is the main image

                                ProductResponse product = new ProductResponse(productId1,productName1,productDescription1, price1, discountedPrice1, imageUrl1);

                                Log.d("SearchProduct", product.getProductName());
                                Log.d("SearchProduct", product.getProductDescription());


                                productListForGrid.add(product); // Add the product to the new list

                            }
                            productGridAdapter.notifyDataSetChanged(); // Notify the adapter that the list has changed
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
