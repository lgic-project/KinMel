package com.example.kinmel.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinmel.CategoryProduct;
import com.example.kinmel.ProductPage;
import com.example.kinmel.R;
import com.example.kinmel.response.CategoryResponse;
import com.example.kinmel.response.ProductResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<CategoryResponse> categoryList;

    public CategoryAdapter(Context context, List<CategoryResponse> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryResponse category = categoryList.get(position);
        String categoryName = category.getCategoryName();

        holder.categoryName.setText(categoryName);
        int width = dpToPx(context, 100);
        int height = dpToPx(context, 150);
        Picasso.get().load(category.getCategoryImage()).resize(width, height).into(holder.categoryImage); holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the productId of the clicked product
                Integer categoryId = category.getCategoryId();
                Log.d("Category ID", String.valueOf(categoryId));

                Intent intent = new Intent(context, CategoryProduct.class);
                intent.putExtra("categoryId", categoryId);
                context.startActivity(intent);


            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
