package com.example.kinmelsellerapp.adapter;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.AutoTransition;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.R;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.example.kinmelsellerapp.request.Order;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

             AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

             builder.setTitle("Delivery Confirmation");
             builder.setMessage("Do you want to deliver the product?");
             builder.setPositiveButton("Deliver", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     sendDeliveryRequest(order.getOrderId(),v);
                     Log.d("Order", "Product Delivered: " + order.getOrderId());
                 }
             });
             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     // Dismiss the dialog
                     dialog.dismiss();
                 }
             });
             AlertDialog dialog = builder.create();
             dialog.show();
         });
    }

    private void sendDeliveryRequest(int orderId, View view) {
        String url = "http://localhost:8080/kinMel/orders?orderItemId=" + orderId;

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(view.getContext());
        String token = sharedPrefManager.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response here
                        Snackbar.make(view, "Delivery Successful", Snackbar.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error here
                        Snackbar.make(view, " Server time out ", Snackbar.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add your Authorization header here
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(view.getContext()).add(stringRequest);
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