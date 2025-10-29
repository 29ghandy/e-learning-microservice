package org.example.courseservice.services.helper;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageSaver {
    public static String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) return null;

        String uploadDir = "course-service/uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String filePath = uploadDir + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.copy(imageFile.getInputStream(), path);

        return filePath;
    }
}
