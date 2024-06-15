package com.example.kinmel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        searchBar.setText(query);

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

        fetchProductsForGrid(query, "", "");

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        RadioGroup radioGroup = headerView.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Reset all radio buttons color
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((RadioButton) group.getChildAt(i)).setTextColor(Color.BLACK);
                }

                // Change the color of the selected radio button
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                selectedRadioButton.setTextColor(Color.RED); // Change this to your desired color
            }
        });

        Button applyButton = headerView.findViewById(R.id.button_apply);
        EditText maxPriceEditText = headerView.findViewById(R.id.edit_text_max_price);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = headerView.findViewById(selectedId);
                String selectedFilter = selectedRadioButton.getText().toString();

                String maxPrice = maxPriceEditText.getText().toString();
                if (!maxPrice.isEmpty()) {
                    Log.d("FilterSelection", "Selected filter: " + selectedFilter + ", Max price: " + maxPrice);
                } else {
                    Log.d("FilterSelection", "Selected filter: " + selectedFilter);
                }
                drawerLayout.closeDrawer(navigationView);
                productListForGrid.clear();
                fetchProductsForGrid(query, selectedFilter, maxPrice);
            }
        });


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
    private void fetchProductsForGrid(String query,String filter, String maxPrice) {
        String url;

        if (filter.equals("Brand")) {
            if (!maxPrice.isEmpty()) {
                url = ApiStatic.FETCH_BRAND_WITH_PRICE_API(query, maxPrice);
            } else {
                url = ApiStatic.FETCH_BRAND_API(query);
            }
        } else if (filter.equals("Product name")) {
            if (!maxPrice.isEmpty()) {
                url = ApiStatic.FETCH_PRODUCT_NAME_WITH_PRICE_API(query, maxPrice);
            } else {
                url = ApiStatic.FETCH_PRODUCT_NAME_API(query);
            }
        } else {
            url = ApiStatic.FETCH_SEARCH_API(query);
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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
