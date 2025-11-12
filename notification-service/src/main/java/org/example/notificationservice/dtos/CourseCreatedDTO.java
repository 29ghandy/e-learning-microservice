package org.example.notificationservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreatedDTO {
    private Long teacherId;
    private String teacherName;
    private String courseName;
    private String description;
    private Double price;
    private String JwtToken;
}
