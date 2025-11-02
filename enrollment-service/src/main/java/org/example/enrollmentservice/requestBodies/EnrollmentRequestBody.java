package org.example.enrollmentservice.requestBodies;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequestBody {
    @NotBlank
    private long studentId;
    @NotBlank
    private long courseId;
    @NotBlank
    private double price;
}
