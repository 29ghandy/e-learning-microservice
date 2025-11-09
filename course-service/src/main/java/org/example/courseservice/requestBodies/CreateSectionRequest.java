package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class CreateSectionRequest {

    @NotNull(message = "Course ID is required")
    @Min(value = 1, message = "Course ID must be greater than 0")
    private Long courseId;

    @NotBlank(message = "Section title is required")
    @Size(min = 3, max = 100, message = "Section title must be between 3 and 100 characters")
    private String sectionTitle;

    @NotNull(message = "Section number is required")
    @Min(value = 1, message = "Section number must be at least 1")
    @Max(value = 1000, message = "Section number cannot exceed 1000")
    private Integer sectionNumber;


}
