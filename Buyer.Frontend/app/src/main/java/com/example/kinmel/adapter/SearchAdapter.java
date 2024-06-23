package com.example.kinmel.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinmel.ProductPage;
import com.example.kinmel.R;
import com.example.kinmel.response.ProductResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<ProductResponse> products;

    public SearchAdapter(Context context, List<ProductResponse> products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        ProductResponse product = products.get(position);
        String productName = product.getProductName();
        holder.productName.setText(productName);
        holder.productPrice.setText("Rs. " +String.valueOf((int)product.getPrice()));
        holder.productDescription.setText(product.getProductDescription());
        holder.productDiscountedPrice.setText("Rs. " +String.valueOf((int)product.getDiscountedPrice()));
        double averageRating = product.getAverageRating();
        int ratingCount = product.getRatingCount();
        TextView productRating = holder.productRating;

        if (averageRating != 0 && ratingCount != 0) {
            String ratingShow = averageRating + "⭐" + "(" + ratingCount + ")";
            productRating.setText(ratingShow);
        } else {
            productRating.setText("");
        }
        Picasso.get().load(product.getImagepath()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the productId of the clicked product
                Integer productId = product.getProductId();
                Log.d("Search Product ID", String.valueOf(productId));
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
                // Do something with the productId (e.g., start a new activity and pass the productId as an extra)
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        // Declare your views
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        TextView productDiscountedPrice;
        TextView productRating;
        ImageView productImage;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name1);
            productDescription = itemView.findViewById(R.id.product_description1);
            productPrice = itemView.findViewById(R.id.product_price1);
            productDiscountedPrice = itemView.findViewById(R.id.product_discounted_price1);
            productRating=itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.product_image1);
        }
    }
}
