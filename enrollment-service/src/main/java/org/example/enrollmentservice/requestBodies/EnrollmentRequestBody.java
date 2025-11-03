package org.example.enrollmentservice.requestBodies;

import ch.qos.logback.core.joran.sanity.Pair;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EnrollmentRequestBody {
    @NotBlank
    @Min(value = 1, message = "Number of hours must be at least 1")
    private long studentId;
    @NotBlank
    private List<Pair<Long,Double>> courseIDs ;
    @NotBlank
    private double price;
    private String mockCardNumber;
}
