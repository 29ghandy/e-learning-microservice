package org.example.courseservice.requestBodies;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateExamRequest {
    private Long courseId;
    private String title;
    private LocalDateTime date;
}
