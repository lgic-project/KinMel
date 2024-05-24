package com.example.kinmel.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinmel.R;
import com.example.kinmel.response.ProductResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder> {
    private Context context;
    private List<ProductResponse> products;

    public ProductGridAdapter(Context context, List<ProductResponse> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductResponse product = products.get(position);
        String productName = product.getProductName();
        holder.productName.setText(productName);
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.productDescription.setText(product.getProductDescription());
        holder.productDiscountedPrice.setText(String.valueOf(product.getDiscountedPrice()));
        Picasso.get().load(product.getImagepath()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the productId of the clicked product
                Integer productId = product.getProductId();
                Log.d("Product ID", String.valueOf(productId));
                // Do something with the productId (e.g., start a new activity and pass the productId as an extra)
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // Declare your views
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        TextView productDiscountedPrice;
        ImageView productImage;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name1);
            productDescription = itemView.findViewById(R.id.product_description1);
            productPrice = itemView.findViewById(R.id.product_price1);
            productDiscountedPrice = itemView.findViewById(R.id.product_discounted_price1);
            productImage = itemView.findViewById(R.id.product_image1);
        }
    }
}