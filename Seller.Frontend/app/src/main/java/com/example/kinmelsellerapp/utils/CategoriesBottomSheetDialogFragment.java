package com.example.kinmelsellerapp.utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.R;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.adapter.CategoriesAdapter;
import com.example.kinmelsellerapp.response.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesBottomSheetDialogFragment extends BottomSheetDialogFragment {


    public OnCategorySelectedListener listener;
    private List<Category> categories=new ArrayList<>();

    public interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       fetchCategories();
    }

    private void fetchCategories() {
        Log.d("FetchCategories", "Fetching categories");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppStatic.FETCH_CATEGORIES, null,
                response -> {
                    try {
                        JSONArray categoriesArray = response.getJSONArray("data");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject categoryObject = categoriesArray.getJSONObject(i);
                            int categoryId = categoryObject.getInt("category_id");
                            String categoryName = categoryObject.getString("category_name");
                            categories.add(new Category(categoryName, categoryId));
                            Log.d("FetchCategories", "Category: " + categoryName + " " + categoryId);
                        }
                        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new CategoriesAdapter(categories.toArray(new Category[0]),this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // handle error
                });

        requestQueue.add(jsonObjectRequest);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_categories, container, false);
        return view;
    }
}