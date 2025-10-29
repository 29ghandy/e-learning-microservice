package org.example.courseservice.requestBodies;

import lombok.Data;

@Data
public class DeleteCourseRequest {

    private long courseId;
    private long teacherId;
    private String role;
}
