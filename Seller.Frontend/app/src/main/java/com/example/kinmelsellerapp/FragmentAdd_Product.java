package com.example.kinmelsellerapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kinmelsellerapp.adapter.ViewPagerAdapter;
import com.example.kinmelsellerapp.utils.CategoriesBottomSheetDialogFragment;

import java.util.ArrayList;

public class FragmentAdd_Product extends Fragment {

    private Button category;
    private Uri imageUri;
    private ArrayList<Uri> chooseImageList ;
    private RelativeLayout pickImageButton;
    private ViewPager viewPager;
    private TextView categoryText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_add_product, container, false);
        category = view.findViewById(R.id.category);
        categoryText= view.findViewById(R.id.categoryShow);
        pickImageButton= view.findViewById(R.id.chooseImage);
        viewPager = view.findViewById(R.id.viewPager);
        chooseImageList = new ArrayList<>();
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesBottomSheetDialogFragment bottomSheet = new CategoriesBottomSheetDialogFragment();
                bottomSheet.setOnCategorySelectedListener(selectedCategory -> {
                    categoryText.setText(selectedCategory.getName());
                    Log.d("CategorySelection", "Selected category id: " + selectedCategory.getId());
                });
                bottomSheet.show(getFragmentManager(), "CategoriesBottomSheet");
            }
        });
         return view;
    }


    private void pickImageFromGallery() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getClipData()!=null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUri= data.getClipData().getItemAt(i).getUri();
                    chooseImageList.add(imageUri);
                    setAdapter();
                }
            }
        }
    }

    private void setAdapter() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), chooseImageList, inflater);
        viewPager.setAdapter(adapter);
    }
}