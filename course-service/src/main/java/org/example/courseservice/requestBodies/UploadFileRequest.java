package org.example.courseservice.requestBodies;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileRequest {
    private String fileName;
    private long chunkIndex;
    private long totalChunks;
    private MultipartFile file;
    private long sectionId;
    private Long courseId;
    private String type;
    private String uploadId;
}
