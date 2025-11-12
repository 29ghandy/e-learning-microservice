package org.example.notificationservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreatedDTO {
    private Long teacherId;
    private Long teacherName;
    private String courseName;
    private String description;
    private Double price;
    private String JwtToken;
}
