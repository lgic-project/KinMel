package com.example.kinmelsellerapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.example.kinmelsellerapp.adapter.ViewPagerAdapter;
import com.example.kinmelsellerapp.utils.CategoriesBottomSheetDialogFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FragmentAdd_Product extends Fragment {

    private Button category;
    private Button addProduct;
    private Uri imageUri;
    private ArrayList<Uri> chooseImageList ;
    private RelativeLayout pickImageButton;
    private ViewPager viewPager;
    private TextView categoryText;
    private Integer categoryId;
    private  String token;
    private SharedPrefManager sharedPrefManager;
    private EditText productName, productDescription,productBrand,productPrice,productDiscountedPrice, productStockQuantity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_add_product, container, false);
        category = view.findViewById(R.id.category);
        categoryText= view.findViewById(R.id.categoryShow);
        pickImageButton= view.findViewById(R.id.chooseImage);
        viewPager = view.findViewById(R.id.viewPager);
        productName = view.findViewById(R.id.productName);
        productDescription = view.findViewById(R.id.productDescription);
        productBrand = view.findViewById(R.id.productBrand);
        productPrice = view.findViewById(R.id.productPrice);
        productDiscountedPrice = view.findViewById(R.id.productDiscountPrice);
        productStockQuantity = view.findViewById(R.id.productStockQuantity);
        addProduct= view.findViewById(R.id.addProduct);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
         token = sharedPrefManager.getToken();
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
                    categoryId = selectedCategory.getId();
                });
                bottomSheet.show(getFragmentManager(), "CategoriesBottomSheet");
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addProductWithValidation();
            }
        });

         return view;
    }

    private void addProductWithValidation() {
        if (productName.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty() ||
                productBrand.getText().toString().isEmpty() || productPrice.getText().toString().isEmpty() ||
                productDiscountedPrice.getText().toString().isEmpty() || productStockQuantity.getText().toString().isEmpty() ||
                categoryText.getText().toString().isEmpty() || chooseImageList.isEmpty()){
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            makeVolleyRequest(); // Add product
        }

    }

    private void makeVolleyRequest() {
        // Create a JSON object for productDetails
        JSONObject productDetails = new JSONObject();
        try {
            productDetails.put("productName", productName.getText().toString());
            productDetails.put("productDescription", productDescription.getText().toString());
            productDetails.put("brand", productBrand.getText().toString());
            productDetails.put("categoryId", categoryId);
            productDetails.put("price", productPrice.getText().toString());
            productDetails.put("discountedPrice", productDiscountedPrice.getText().toString());
            productDetails.put("stockQuantity", productStockQuantity.getText().toString());
        } catch (JSONException e) {
            Log.d("AddProduct", "Failed to create JSON object");
            Log.d("AddProduct", e.getMessage());
            e.printStackTrace();
        }

        // Create a RequestBody for productDetails
        RequestBody productDetailsPart = RequestBody.create(MediaType.parse("application/json"), productDetails.toString());

        // Create a MultipartBody builder
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"productDetails\""),
                        productDetailsPart);

        // Add images to the multipart request
        for (int i = 0; i < chooseImageList.size(); i++) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(chooseImageList.get(i));
                byte[] imageData = toByteArray(inputStream);
                String mimeType = getContext().getContentResolver().getType(chooseImageList.get(i));
                String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                String filename = "image_" + i + "." + extension;

                // Create a RequestBody for the image
                RequestBody imagePart = RequestBody.create(MediaType.parse(mimeType), imageData);

                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"productImages\"; filename=\"" + filename + "\""),
                        imagePart);
            } catch (IOException e) {
                Log.d("AddProduct", "Failed to add image");
                Log.d("AddProduct", e.getMessage());
                e.printStackTrace();
            }
        }

        // Build the multipart request body
        RequestBody requestBody = builder.build();

        // Create a POST request
        Request request = new Request.Builder()
                .url(AppStatic.ADD_PRODUCTS)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        // Send the request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        String message = json.getString("message");

                        // Show toast on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                resetForm();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AddProduct", "Failed to add product");
                Log.d("AddProduct", e.getMessage());
                // Handle error
            }
        });
    }

    private void resetForm() {
        productName.setText("");
        productDescription.setText("");
        productBrand.setText("");
        productPrice.setText("");
        productDiscountedPrice.setText("");
        productStockQuantity.setText("");
        categoryText.setText("");
        categoryId = null;
        chooseImageList.clear();
        viewPager.setAdapter(null);
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
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    private void setAdapter() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), chooseImageList, inflater);
        viewPager.setAdapter(adapter);
    }
}