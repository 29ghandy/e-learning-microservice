package org.example.courseservice.services;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.requestBodies.UploadFileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class FileService {
    private final String UPLOAD_DIR = System.getProperty("user.dir") +  "/course-service/uploads";

    public ResponseEntity<?> addFile(UploadFileRequest request) throws IOException {
        MultipartFile file = request.getFile();

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        File chunkFile = new File(UPLOAD_DIR + "/" + request.getFileName() + "." + request.getChunkIndex());
        file.transferTo(chunkFile);

        if (request.getChunkIndex() == request.getTotalChunks() - 1) {
            mergeChunks(request.getFileName(), request.getTotalChunks());
        }

        return ResponseEntity.ok("Chunk " + request.getChunkIndex() + " uploaded successfully");
    }

    private void mergeChunks(String fileName, long totalChunks) throws IOException {
        File mergedFile = new File(UPLOAD_DIR + "/" + fileName);
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(mergedFile))) {
            for (int i = 0; i < totalChunks; i++) {
                File chunk = new File(UPLOAD_DIR + "/" + fileName + "." + i);
                Files.copy(chunk.toPath(), mergingStream);
                chunk.delete();
            }
        }
    }
}
