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
import com.example.kinmelsellerapp.request.Order;

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
                TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                discLayout.setVisibility(View.VISIBLE);
                motherLayout.setBackgroundColor(Color.parseColor("#724CAF50"));
                arrowImg.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                discLayout.setVisibility(View.GONE);
                motherLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                arrowImg.setImageResource(R.drawable.up);
            }
        });


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        // Set the data to the views here
        // For example:
        holder.address1.setText(order.getAddress1());
         holder.orderProductName.setText(order.getProductName());
         holder.orderProductQuantity.setText(String.valueOf(order.getQuantity()));
         holder.orderProductImage.setImageResource(R.drawable.ic_launcher_background);
         holder.orderProductName1.setText(order.getProductName());
         holder.orderAccept.setOnClickListener(v -> {
             Log.d("Order", "Order Accepted"+order.getOrderId());
         });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderProductImage;
        TextView orderProductName,orderProductName1;
        TextView address1;
        TextView orderProductQuantity;
        Button orderAccept;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            address1 = itemView.findViewById(R.id.address1);
            orderProductImage = itemView.findViewById(R.id.orderProductImage);
            orderProductName = itemView.findViewById(R.id.orderProductName);
            orderProductName1 = itemView.findViewById(R.id.orderProductName1);
            orderProductQuantity = itemView.findViewById(R.id.orderProductQuantity);
            orderAccept = itemView.findViewById(R.id.acceptButton);
        }
    }
}