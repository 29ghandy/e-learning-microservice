package org.example.userservice.services.helper.designPatterns.userSaving;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageSaver {
      static String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) return null;

        String uploadDir = "user-service/uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String filePath = uploadDir + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.copy(imageFile.getInputStream(), path);

        return filePath;
    }
}
