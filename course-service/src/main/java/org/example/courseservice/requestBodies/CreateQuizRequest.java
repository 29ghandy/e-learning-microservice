package org.example.courseservice.requestBodies;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateQuizRequest {
    private Long sectionId;
    private String title;
    private LocalDateTime date;
}
