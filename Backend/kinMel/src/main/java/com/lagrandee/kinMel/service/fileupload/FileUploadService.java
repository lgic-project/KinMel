package com.lagrandee.kinMel.service.fileupload;

import com.lagrandee.kinMel.exception.NotInsertedException;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {
//    private final String UPLOAD_DIR = "C:\\Users\\bisha\\OneDrive\\Desktop\\KinMel\\Backend\\kinMel\\productImages";

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
        Resource resource = new ClassPathResource("static/product_images");
        File file = resource.getFile();
        return file.getAbsolutePath();
    }
}
