package org.example.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "enrollment-service", url = "http://localhost:8083")
public interface EnrollmentClient {
    @PostMapping("api/enrollment/students/by-teacher/{teacherId}")
    List<Long> getStudentsByTeacher(@PathVariable Long teacherId, @RequestBody List<Long> courseIds);
}
