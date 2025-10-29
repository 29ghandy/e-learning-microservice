package org.example.courseservice.controllers.student;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Course;
import org.example.courseservice.services.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class StudentCourseController {
    private final CourseService courseService;
    @GetMapping("/")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
}
