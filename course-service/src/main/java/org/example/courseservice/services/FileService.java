package org.example.courseservice.services;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Section;
import org.example.courseservice.models.SectionFile;
import org.example.courseservice.repositories.SectionFileRepository;
import org.example.courseservice.repositories.SectionRepository;
import org.example.courseservice.requestBodies.UploadFileRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final String BASE_DIR = System.getProperty("user.dir") + "/course-service/uploads";
    private final String TEMP_DIR = BASE_DIR + "/temp";
    private final String FINAL_DIR = BASE_DIR + "/final";
    private final SectionRepository sectionRepository;
    private final SectionFileRepository sectionFileRepository;

    public ResponseEntity<?> addFile(UploadFileRequest request) throws IOException {
        MultipartFile file = request.getFile();

        Files.createDirectories(Paths.get(TEMP_DIR));
        Files.createDirectories(Paths.get(FINAL_DIR));

        String uploadId = request.getUploadId();
        if (uploadId == null || uploadId.isBlank()) {
            if (request.getChunkIndex() != 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing uploadId for non-first chunk. First chunk must be uploaded without uploadId to receive one from server."));
            }
            uploadId = UUID.randomUUID().toString();
        }

        Path uploadFolder = Paths.get(TEMP_DIR, uploadId);
        Files.createDirectories(uploadFolder);

        String chunkFileName = "chunk_" + request.getChunkIndex() + ".part";
        Path chunkPath = uploadFolder.resolve(chunkFileName);

        file.transferTo(chunkPath.toFile());

        if (request.getChunkIndex() == request.getTotalChunks() - 1) {
            mergeChunks(request, uploadId);
        }

        return ResponseEntity.ok(Map.of(
                "message", "Chunk " + request.getChunkIndex() + " uploaded successfully.",
                "uploadId", uploadId
        ));
    }

    private void mergeChunks(UploadFileRequest request, String uploadId) throws IOException {
        Path uploadFolder = Paths.get(TEMP_DIR, uploadId);

        if (!Files.exists(uploadFolder) || !Files.isDirectory(uploadFolder)) {
            throw new FileNotFoundException("Temporary upload folder not found: " + uploadFolder.toAbsolutePath());
        }

        String original = request.getFileName() == null ? "file" : request.getFileName();
        String sanitized = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String finalFileName = String.format("course%d_section%d_%s",
                request.getCourseId(), request.getSectionId(), sanitized);

        Path mergedFile = Paths.get(FINAL_DIR, finalFileName);

        try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(mergedFile.toFile()))) {
            for (int i = 0; i < request.getTotalChunks(); i++) {
                Path chunk = uploadFolder.resolve("chunk_" + i + ".part");
                if (!Files.exists(chunk)) {
                    throw new FileNotFoundException("Missing chunk: " + chunk.toAbsolutePath());
                }
                Files.copy(chunk, mergingStream);
                Files.delete(chunk);
            }
        }

        deleteDirectoryRecursively(uploadFolder);

        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow();
        SectionFile sectionFile = new SectionFile();
        sectionFile.setSection(section);
        sectionFile.setType(request.getType());
        sectionFile.setPath(mergedFile.toAbsolutePath().toString());
        sectionFile.setName(request.getFileName());
        sectionFileRepository.save(sectionFile);

        section.addFile(sectionFile);
        sectionRepository.save(section);
    }

    private void deleteDirectoryRecursively(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public ResponseEntity<?> downloadFile(Long id) throws IOException {
        Optional<SectionFile> secFile = sectionFileRepository.findById(id);
        if (secFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        SectionFile sectionFile = secFile.get();
        if (sectionFile.getType() != null &&
                sectionFile.getType().equalsIgnoreCase("video")) {
            return ResponseEntity.status(403)
                    .body(null);
        }

        File file = new File(sectionFile.getPath());
        if (!file.exists()) {
            throw new RuntimeException("Physical file not found on disk");
        }

        Resource resource = new FileSystemResource(file);
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + sectionFile.getName() + "\"")
                .body(resource);
    }

}