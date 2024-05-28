package com.example.kinmel.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static String encodeImageToBase64(InputStream inputStream) {
        try {
            // Decode the input stream into a Bitmap object
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Create a ByteArrayOutputStream to store the compressed image data
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Get the Base64 encoded string from the ByteArrayOutputStream
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Close the input stream and ByteArrayOutputStream
            inputStream.close();
            byteArrayOutputStream.close();

            return encodedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}