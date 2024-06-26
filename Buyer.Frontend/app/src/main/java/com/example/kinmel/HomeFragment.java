package com.example.kinmel;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.adapter.CategoryAdapter;
import com.example.kinmel.adapter.MySingleton;
import com.example.kinmel.adapter.ProductAdapterMain;
import com.example.kinmel.adapter.ProductGridAdapter;
import com.example.kinmel.adapter.SpaceItemDecoration;
import com.example.kinmel.response.CategoryResponse;
import com.example.kinmel.response.ProductResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productContainer,productContainerForGrid,categoryContainer;
    private List<ProductResponse> productList = new ArrayList<>();
    private List<ProductResponse> productListForGrid = new ArrayList<>();
    private List<CategoryResponse> categoryList = new ArrayList<>();
    private ProductAdapterMain productAdapter;
    private ProductGridAdapter productGridAdapter;

    private CategoryAdapter categoryAdapter;
    private ShimmerFrameLayout shimmerViewContainer;
    private ShimmerFrameLayout shimmerViewContainer1;
    private ShimmerFrameLayout shimmerViewContainer2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmerViewContainer1 = view.findViewById(R.id.shimmer_view_container1);
        shimmerViewContainer2 = view.findViewById(R.id.shimmer_view_category);

        EditText searchBox = view.findViewById(R.id.search_box);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = v.getText().toString();
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });



        // Start shimmer effect when page starts
        shimmerViewContainer.startShimmer();
        shimmerViewContainer1.startShimmer();
        shimmerViewContainer2.startShimmer();

        categoryContainer=view.findViewById(R.id.categoryContainer);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categoryContainer.setLayoutManager(layoutManager1);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        categoryContainer.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        categoryContainer.setAdapter(categoryAdapter);

        productContainer = view.findViewById(R.id.product_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productContainer.setLayoutManager(layoutManager);
        productContainer.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        productAdapter = new ProductAdapterMain(getActivity(), productList);
        productContainer.setAdapter(productAdapter);


        // New RecyclerView for grid
        productContainerForGrid = view.findViewById(R.id.product_container_for_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); // 2 columns in grid
        productContainerForGrid.setLayoutManager(gridLayoutManager);
        productGridAdapter = new ProductGridAdapter(getActivity(), productListForGrid); // Use the new list here
        productContainerForGrid.setAdapter(productGridAdapter);
        int spacingInPixels2 = getResources().getDimensionPixelSize(R.dimen.spacing); // Define your spacing size in dimens.xml
        productContainerForGrid.addItemDecoration(new SpaceItemDecoration(spacingInPixels2));

        fetchProducts();
        fetchSlider(view);
        fetchProductsForGrid();
        fetchCategory();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform logout operation here
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.apply();

                                Intent intent = new Intent(getActivity(), BuyerLogin.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return view;
    }

    private void fetchCategory() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.FETCH_ALL_CATEGORY_API, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject categoryObject = data.getJSONObject(i);
                                int categoryId = categoryObject.getInt("category_id");
                                String categoryName = categoryObject.getString("category_name");
                                String categoryDescription = categoryObject.getString("category_description");
                                String imagePath = ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API+categoryObject.getString("imagePath");

                                CategoryResponse category = new CategoryResponse(categoryId,categoryName,categoryDescription, imagePath);
                                    Log.d("Category Name", imagePath);
                                // Add the category to the list
                                    categoryList.add(category);
                            }
                            shimmerViewContainer2.stopShimmer();
                            shimmerViewContainer2.setVisibility(View.GONE);
                            // Notify the adapter that the data has been fetched
                            categoryAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void fetchProductsForGrid() {
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, ApiStatic.FETCH_PRODUCT_HOME_API_GRID, null, new Response.Listener<JSONObject>() {

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
                            shimmerViewContainer.stopShimmer();
                            shimmerViewContainer.setVisibility(View.GONE);
                            productGridAdapter.notifyDataSetChanged();

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

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest1);
    }


    private void fetchSlider(View view) {
        ArrayList<SlideModel> imageList = new ArrayList<>();

// imageList.add(new SlideModel("String Url" or R.drawable));

// imageList.add(new SlideModel("String Url" or R.drawable, "title")); // You can add title

        int imageResource = R.drawable.chatting; // replace 'your_image' with your image file name without extension
        String uri = "android.resource://" + getActivity().getPackageName() + "/" + imageResource;

        imageList.add(new SlideModel("https://images.pexels.com/photos/259200/pexels-photo-259200.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Big Sale", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://images.unsplash.com/photo-1607083206869-4c7672e72a8a?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fHNhbGV8ZW58MHx8MHx8fDA%3D",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://images.unsplash.com/photo-1647221598398-934ed5cb0e4f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTR8fGVjb21tZXJjZSUyMGFkc3xlbnwwfDB8MHx8fDA%3D",ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);
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
                                Integer productId = productObject.getInt("productId");;
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

                                ProductResponse product = new ProductResponse(productName, price, discountedPrice, imageUrl,productId);
                                productList.add(product);
//                                shimmerViewContainer.stopShimmer();
                            }

                            productAdapter.notifyDataSetChanged();
                            shimmerViewContainer1.stopShimmer();
                            shimmerViewContainer1.setVisibility(View.GONE);

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