package com.example.kinmelsellerapp.adapter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.transition.TransitionManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.AutoTransition;
import com.example.kinmelsellerapp.R;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.request.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_orders, parent, false);
        LinearLayout motherLayout = view.findViewById(R.id.motherLayout);
        RelativeLayout itemClicked = view.findViewById(R.id.itemClicked);
        ImageView arrowImg = view.findViewById(R.id.arrowImg);
        LinearLayout discLayout = view.findViewById(R.id.discLayout);

        itemClicked.setOnClickListener(v -> {
            if (discLayout.getVisibility() == View.GONE){
                arrowImg.setImageResource(R.drawable.up);
//                TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                discLayout.setVisibility(View.VISIBLE);
//                motherLayout.setBackgroundColor(Color.parseColor("#724CAF50"));
            } else {
                arrowImg.setImageResource(R.drawable.down);
//                TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                discLayout.setVisibility(View.GONE);
//                motherLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        // Set the data to the views here
        // For example:
        holder.orderHeadingProductQuantity.setText(String.valueOf(order.getQuantity()));
        holder.orderProductHeadingName.setText(order.getProductName());
        Picasso.get().load(AppStatic.FETCH_PRODUCT_IMAGE_HOME_API + order.getImagePath()).into(holder.orderProductImage);
        holder.orderProductNameValue.setText(order.getProductName());
        holder.orderIdValue.setText(String.valueOf(order.getGroupOrderId()));
        holder.orderPersonNameValue.setText(order.getPersonName());
        holder.orderAddressValue.setText(order.getPersonAddress());
        holder.orderProductPhoneValue.setText(order.getPersonPhoneNumber());
        holder.orderProductQuantityValue.setText(String.valueOf(order.getQuantity()));
        holder.orderProductAmountValue.setText("Rs. "+String.valueOf(order.getPrice()));
        holder.orderProductPaymentMethodValue.setText(order.getPaymentMethod());
         holder.orderAccept.setOnClickListener(v -> {
             Log.d("Order", "Order Accepted"+order.getOrderId());
         });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderHeadingProductQuantity,orderProductHeadingName;
        ImageView orderProductImage;
       TextView orderProductNameValue,orderIdValue,orderPersonNameValue,orderAddressValue,orderProductPhoneValue;
        TextView orderProductQuantityValue,orderProductAmountValue,orderProductPaymentMethodValue;
        Button orderAccept;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderHeadingProductQuantity = itemView.findViewById(R.id.orderHeadingProductQuantity);
            orderProductHeadingName = itemView.findViewById(R.id.orderProductHeadingName);
            orderProductImage = itemView.findViewById(R.id.orderProductImage);
            orderProductNameValue = itemView.findViewById(R.id.orderProductNameValue);
            orderIdValue = itemView.findViewById(R.id.orderIdValue);
            orderPersonNameValue = itemView.findViewById(R.id.orderPersonNameValue);
            orderAddressValue = itemView.findViewById(R.id.orderAddressValue);
            orderProductPhoneValue = itemView.findViewById(R.id.orderProductPhoneValue);
            orderProductQuantityValue = itemView.findViewById(R.id.orderProductQuantityValue);
            orderProductAmountValue = itemView.findViewById(R.id.orderProductAmountValue);
            orderProductPaymentMethodValue = itemView.findViewById(R.id.orderProductPaymentMethodValue);
            orderAccept = itemView.findViewById(R.id.acceptButton);
        }
    }
}