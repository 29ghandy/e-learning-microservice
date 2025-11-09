package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StreamCourseRequest {

    @Min(value = 1, message = "Course ID must be greater than 0")
    private Long courseId;

    @Min(value = 1, message = "File ID must be greater than 0")
    private Long fileId;

    @Min(value = 1, message = "Student ID must be greater than 0")
    private Long studentId;

    @NotBlank(message = "Range header is required")
    @Pattern(
            regexp = "^bytes=\\d*-\\d*$",
            message = "Invalid range format. Expected format: bytes=start-end (e.g., bytes=0-1023 or bytes=1000-)"
    )
    private String range;
}
