package com.example.kinmel.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmel.Interface.OnQuantityChangeListener;
import com.example.kinmel.Product;
import com.example.kinmel.R;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnQuantityChangeListener onQuantityChangeListener;

    public ProductAdapter(List<Product> productList, OnQuantityChangeListener onQuantityChangeListener) {
        this.productList = productList;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("Price: Rs." + product.getPrice());
        holder.productDiscountPrice.setText("Discount: Rs." + product.getDiscountedPrice());
        holder.quantity.setText(String.valueOf(product.getQuantity()));
        holder.productTotalPrice.setText("Total: Rs." + product.getTotal());
        Picasso.get().load(product.getImage()).into(holder.productImage);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(product.isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                product.setSelected(isChecked);
                onQuantityChangeListener.updateTotalPrice();
                onQuantityChangeListener.updateCheckoutButtonText();
            }
        });
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuantityChangeListener.onQuantityChange(product.getCartId(), "add");
            }
        }); holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getQuantity() > 0) {
                    onQuantityChangeListener.onQuantityChange(product.getCartId(), "sub");
                }
                Log.d("ProductAdapter", "Minus Button Clicked"+product.getCartId()+"Quantity: "+product.getQuantity());
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDiscountPrice, quantity,productTotalPrice;
        ImageView productImage;
        CheckBox checkBox;
        ImageButton plusButton, minusButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDiscountPrice = itemView.findViewById(R.id.productDiscountPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productTotalPrice = itemView.findViewById(R.id.productTotalPrice);
            productImage = itemView.findViewById(R.id.productImage);
            checkBox = itemView.findViewById(R.id.checkBox);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }
}

