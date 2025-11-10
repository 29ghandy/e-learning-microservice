package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class StreamCourseRequest implements Serializable {

    @Min(value = 1, message = "Course ID must be greater than 0")
    private Long courseId;

    @Min(value = 1, message = "File ID must be greater than 0")
    private Long fileId;

    @Min(value = 1, message = "Student ID must be greater than 0")
    private Long studentId;

    @Nullable
    @Pattern(
            regexp = "^bytes=\\d*-\\d*$",
            message = "Invalid range format. Expected format: bytes=start-end (e.g., bytes=0-1023 or bytes=1000-)"
    )
    private String range;
}
