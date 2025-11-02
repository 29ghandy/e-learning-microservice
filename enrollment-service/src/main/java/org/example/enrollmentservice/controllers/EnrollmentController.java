package org.example.enrollmentservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.models.Enrollment;
import org.example.enrollmentservice.requestBodies.EnrollmentRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {
   @PostMapping("/payment")
    public ResponseEntity<Enrollment> payForCourse(@RequestBody EnrollmentRequestBody requestBody) {
       return null;
   }
}
