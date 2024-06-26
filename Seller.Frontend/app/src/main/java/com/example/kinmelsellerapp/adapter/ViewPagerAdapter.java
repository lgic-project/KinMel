package com.example.kinmelsellerapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.kinmelsellerapp.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Uri> imageUrls;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context, ArrayList<Uri> imageUrls, LayoutInflater layoutInflater) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view = layoutInflater.inflate(R.layout.showimageslayout, container, false);
        ImageView imageView = view.findViewById(R.id.uploadImage);
        imageView.setImageURI(imageUrls.get(position));
        container.addView(view);
        return view;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((RelativeLayout) object).removeView((container));
    }


}
