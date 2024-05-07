package com.lagrandee.kinMel.service.fileupload;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {
    private final String UPLOAD_DIR = "C:\\Users\\bisha\\OneDrive\\Desktop\\KinMel\\Backend\\kinMel\\productImages";

    public List<String> uploadFiles(MultipartFile[] files) {
        List<String> filePaths = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                Path uploadPath = Paths.get(UPLOAD_DIR, uniqueFileName);

                try {
                    Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
                    filePaths.add(uploadPath.toString());
                } catch (IOException e) {
                    // Handle file upload error
                    e.printStackTrace();
                }
            }
        }

        return filePaths;
    }
}
