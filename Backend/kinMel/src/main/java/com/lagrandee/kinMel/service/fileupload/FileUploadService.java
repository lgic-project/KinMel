package com.lagrandee.kinMel.service.fileupload;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {
    public static List<String> saveMultipartImages(MultipartFile[] files) throws IOException {
        List<String> filePaths = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileExtension = getFileExtension(file.getOriginalFilename());
                    String fileName = UUID.randomUUID().toString() + fileExtension;
                    String absoluteImagePath = getProfilePath();

                    File parentDir = new File(absoluteImagePath);
                    if (!parentDir.exists()) {
                        if (!parentDir.mkdirs()) {
                            throw new IOException("Failed to create directories: " + parentDir.getAbsolutePath());
                        }
                    }

                    File imageFile = new File(parentDir, fileName);
                    file.transferTo(imageFile);
                    String relativePath = "product_images/" + fileName;
                    filePaths.add(relativePath);
                }
            }
        }

        return filePaths;
    }

    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return ""; // No file extension found
        }
        return filename.substring(lastDotIndex);
    }

    public static String getProfilePath() throws IOException {
        Path currentPath = Paths.get("").toAbsolutePath();
        Path resourcePath = currentPath.resolve("src/main/resources/static/product_images");
        return resourcePath.toFile().getAbsolutePath();
    }
}