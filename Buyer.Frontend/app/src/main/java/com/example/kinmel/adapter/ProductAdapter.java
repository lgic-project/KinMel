package com.example.kinmel.adapter;

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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductResponse> products;

    public ProductAdapter(List<ProductResponse> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductResponse product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productDiscountedPriceTextView;
        TextView productPriceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productDiscountedPriceTextView = itemView.findViewById(R.id.product_discounted_price);
            productPriceTextView = itemView.findViewById(R.id.product_price);
        }

        public void bind(ProductResponse product) {
            productNameTextView.setText(product.getProductName());
            productDiscountedPriceTextView.setText("$" + product.getDiscountedPrice());
            productPriceTextView.setText("$" + product.getPrice());
            // Load the product image using a library like Glide or Picasso
            Picasso.get()
                    .load(product.getProductImages().get(0))
                    .into(productImageView);
        }
    }
}
