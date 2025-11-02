package org.example.courseservice.controllers.student;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.indexies.CourseIndex;
import org.example.courseservice.models.Course;
import org.example.courseservice.services.CourseService;
import org.example.courseservice.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/course/student")
@RequiredArgsConstructor
public class StudentCourseController {
    private final CourseService courseService;
    private final FileService fileService;

    @GetMapping("/")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/search")
    public List<CourseIndex> searchCourses(@RequestParam String search) {
        return courseService.findByNameContainingIgnoreCase(search);
    }

    @GetMapping("/file/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) throws IOException {
        try {
            return fileService.downloadFile(id);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
