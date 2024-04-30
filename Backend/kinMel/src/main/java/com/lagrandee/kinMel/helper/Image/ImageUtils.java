package com.lagrandee.kinMel.helper.Image;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class ImageUtils {
    public static String saveDecodedImage(String base64Image, String imageUploadPath,String imageFormat) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null; // Handle case of no image provided
        }

        // Decode Base64 image data
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + imageFormat; // Adjust extension based on image type

        // Create the image file path
        String filePath = imageUploadPath + File.separator + fileName;

        // Save decoded image to the file system
        File imageFile = new File(filePath);
        FileUtils.writeByteArrayToFile(imageFile, decodedBytes);

        return filePath;
    }

    public static String encodeImageToBase64(String imagePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }


}
