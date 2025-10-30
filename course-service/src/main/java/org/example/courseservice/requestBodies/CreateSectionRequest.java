package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSectionRequest {
    @NotBlank
    private Long courseId;
    @NotBlank
    private String sectionTitle;
    private Integer sectionNumber;
    private Integer sectionNumberOfHours;
}
