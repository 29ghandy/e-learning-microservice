package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateDiscountRequest {
    @NotBlank
    @Min(value = 1, message = "Number of hours must be at least 1")
    private double discountPercentage;
    @NotBlank
    private long courseId;
    private long numberOfDays;
    private Long discountNumberOfMembers;
}
