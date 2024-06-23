package com.example.kinmel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinmel.R;
import com.example.kinmel.StaticFiles.ApiStatic;
import com.example.kinmel.response.Order;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderHistoryAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText(String.valueOf(order.getOrderId()));
//        holder.orderDate.setText(order.getOrderedAt());
        String orderDateStr = order.getOrderedAt(); // "2024-06-09 11:47:05.383"
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        Date orderDate = null;
        try {
            orderDate = originalFormat.parse(orderDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (orderDate != null) {
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, h:mm a", Locale.US);
            String formattedDate = newFormat.format(orderDate);
            holder.orderDate.setText(formattedDate); // "Jun 09, 2024, 11:47 AM"
        }
        holder.orderProductName.setText( order.getProductName());
        holder.orderTotal.setText("Rs. " + order.getTotalPrice());
        holder.orderStatus.setText(order.getOrderStatus());
        holder.orderQuantity.setText(order.getQuantity());

        // Load the image from the URL
        Picasso.get().load(ApiStatic.FETCH_PRODUCT_IMAGE_HOME_API + order.getImagePath()).into(holder.orderImage);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderId;
        public TextView orderDate;
        public TextView orderProductName;
        public TextView orderQuantity;
        public TextView orderTotal;
        public TextView orderStatus;
        public ImageView orderImage;

        public ViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderProductName = itemView.findViewById(R.id.order_name);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderStatus = itemView.findViewById(R.id.order_Status);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
            orderImage = itemView.findViewById(R.id.order_image);
        }
    }
}