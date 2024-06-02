package com.lagrandee.kinMel.helper.Image;


import com.lagrandee.kinMel.exception.NotInsertedException;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class ImageUtils {
    public static String saveDecodedImage(String base64Image, String absoluteImagePath, String imageFormat,String returnPath) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Image data is null or empty");
        }
        try {
            // Decode Base64 image data
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            // Generate a unique filename
            String fileName = UUID.randomUUID().toString() + "." + imageFormat;

            // Ensure the directories exist
            File parentDir = new File(absoluteImagePath);
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("Failed to create directories: " + parentDir.getAbsolutePath());
                }
            }

            // Construct the absolute file path
            File imageFile = new File(parentDir, fileName);

            // Save decoded image to the file system
            FileUtils.writeByteArrayToFile(imageFile, decodedBytes);

            // Return the relative file path for further use (e.g., referencing in HTML)
            return returnPath + fileName;
        } catch (IOException e) {
            throw new NotInsertedException("Image cannot be saved", e);
        }
    }



    public static String encodeImageToBase64(String relativeImagePath) throws IOException {
        try {
            // Construct the absolute path from the relative path
            String absoluteImagePath = new ClassPathResource("static/" + relativeImagePath).getFile().getAbsolutePath();

            // Read the file content
            byte[] fileContent = Files.readAllBytes(Paths.get(absoluteImagePath));

            // Encode the file content to Base64
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            return "No Image Found";
        }
    }



}
