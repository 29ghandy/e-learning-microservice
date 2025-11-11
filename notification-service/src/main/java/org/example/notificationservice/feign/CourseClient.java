package org.example.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/teacher/get-teacher-courses/{teacherId}")
    List<Long> getTeacherCourses(@PathVariable Long teacherId);
}
