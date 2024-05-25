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

import com.example.kinmel.ProductPage;
import com.example.kinmel.R;
import com.example.kinmel.response.ProductResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapterMain extends RecyclerView.Adapter<ProductAdapterMain.ProductViewHolder> {

    private Context context;
    private List<ProductResponse> productList;

    public ProductAdapterMain(Context context, List<ProductResponse> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductResponse product = productList.get(position);
        String productName = product.getProductName();
        if (productName.length() > 12) {
            productName = productName.substring(0, 12) + "...";
        }
        holder.productName.setText(productName);
        holder.productPrice.setText("Rs. " +String.valueOf((int)product.getPrice()));
        holder.productDiscountedPrice.setText("Rs. " +String.valueOf((int)product.getDiscountedPrice()));
        Picasso.get().load(product.getImagepath()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the productId of the clicked product
                Integer productId = product.getProductId();
                Log.d("Product ID", String.valueOf(productId));
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
                // Do something with the productId (e.g., start a new activity and pass the productId as an extra)
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productDiscountedPrice;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productDiscountedPrice = itemView.findViewById(R.id.product_discounted_price);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}