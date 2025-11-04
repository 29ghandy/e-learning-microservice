package org.example.enrollmentservice.requestBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {
    private Long id;
    private Double price;
}