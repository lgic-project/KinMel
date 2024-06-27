package com.example.kinmelsellerapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinmelsellerapp.Static.AppStatic;
import com.example.kinmelsellerapp.Static.SharedPrefManager;
import com.example.kinmelsellerapp.utils.ImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FragmentRequestCategory extends Fragment {

    private EditText categoryName, categoryDescription;
    private ImageView categoryImage;
    private Uri selectedImageUri;
    private Button requestCategoryButton;
    private SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment first
        View view = inflater.inflate(R.layout.activity_request_category, container, false);
        categoryName= view.findViewById(R.id.reqCategoryName);
        categoryDescription= view.findViewById(R.id.reqCategoryDescription);
        categoryImage= view.findViewById(R.id.reqCategoryImage);
        requestCategoryButton= view.findViewById(R.id.btnRequestCategory);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Requesting Category...");
        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

    requestCategoryButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestForCategory();
        }
    });


        return view;
    }

    private void requestForCategory() {
        String name=categoryName.getText().toString();
        String description=categoryDescription.getText().toString();
        String imageFormat = null;
        String encodedImage = "";

        if(name.isEmpty() || description.isEmpty() || selectedImageUri==null){
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
             encodedImage = ImageUtils.encodeImageToBase64(inputStream);
            imageFormat = getMimeType(selectedImageUri);
             if (imageFormat != null && imageFormat.contains("/")) {
                imageFormat = imageFormat.substring(imageFormat.indexOf("/") + 1);
            }
        } catch (Exception e) {
            Log.d("ImageError", e.getMessage());
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error in image ", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("categoryName", name);
            requestBody.put("categoryDescription", description);
            requestBody.put("categoryImage", encodedImage);
            requestBody.put("imageFormat", imageFormat);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, AppStatic.REQUEST_CATEGORY, requestBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 200) {
                                String message = response.getString("data");
                                resetForm();
                                progressDialog.dismiss();
                                alertMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        resetForm();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPrefManager.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

        progressDialog.dismiss();


    }

    private void alertMessage(String message) {

        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage("Your category has been successfully requested")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        categoryDescription.clearFocus();
        categoryName.clearFocus();
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    categoryImage.setImageURI(uri);
                    selectedImageUri = uri;
                }
            });
    private void resetForm() {
        categoryName.setText("");
        categoryDescription.setText("");
        categoryImage.setImageResource(R.drawable.camera);
    }


}