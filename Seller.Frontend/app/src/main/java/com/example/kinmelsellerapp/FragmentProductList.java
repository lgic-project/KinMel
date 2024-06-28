package com.example.kinmelsellerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kinmelsellerapp.adapter.OrdersAdapter;
import com.example.kinmelsellerapp.request.Order;

import java.util.ArrayList;
import java.util.List;

public class FragmentProductList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_product, container, false);
        RecyclerView rvOrders = view.findViewById(R.id.orderContainer);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        List<Order> orders = getOrders();
        OrdersAdapter adapter = new OrdersAdapter(orders);
        rvOrders.setAdapter(adapter);
        return view;
    }

    private List<Order> getOrders() {
       List<Order> orders = new ArrayList<>();
         orders.add(new Order("1", "Product 1", 1, "Person 1", "Address 1", "1234567890", "imagePath1", 100, "Khalti"));
         orders.add(new Order("2", "Product 2", 2, "Person 2", "Address 2", "1234567890", "imagePath2", 200, "Cash On Delivery"));
            orders.add(new Order("3", "Product 3", 3, "Person 3", "Address 3", "1234567890", "imagePath3", 300, "Khalti"));
            return orders;
    }
}