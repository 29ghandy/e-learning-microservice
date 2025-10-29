package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDiscountRequest {
    @NotBlank
    @Min(value = 1, message = "Number of hours must be at least 1")
    private long numberOfDays;
    @NotBlank
    @Min(value = 1, message = "Number of hours must be at least 1")
    private double discountPercentage;
    private String role;
    private long courseId;
}
