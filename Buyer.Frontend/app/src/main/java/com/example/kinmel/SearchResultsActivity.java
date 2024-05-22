package com.example.kinmel;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private GridView gridView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable title and back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search Results");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText searchBox = findViewById(R.id.search_box);
        gridView = findViewById(R.id.gridView);

        // Populate product list
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "$10", R.drawable.images));
        productList.add(new Product("Product 2", "$20", R.drawable.images));
        productList.add(new Product("Product 3", "$30", R.drawable.images));

        // Initialize filtered list and adapter
        filteredList = new ArrayList<>(productList);
        productAdapter = new ProductAdapter(this, filteredList);
        gridView.setAdapter(productAdapter);

        // Add text watcher to search box
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        // Handle grid item clicks
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Product clickedProduct = filteredList.get(position);
            // Handle item click
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterProducts(String query) {
        filteredList.clear();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.notifyDataSetChanged();
    }
}
