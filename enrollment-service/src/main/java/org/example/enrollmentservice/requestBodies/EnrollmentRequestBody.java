package org.example.enrollmentservice.requestBodies;

import ch.qos.logback.core.joran.sanity.Pair;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EnrollmentRequestBody {
    @Min(value = 1, message = "Student ID must be at least 1")
    private long studentId;

    @NotEmpty(message = "Course list cannot be empty")
    private List<CourseInfo> courseIDs;

    @NotNull(message = "Total price is required")
    private Double totalPrice;

    private String mockCardNumber;
}
