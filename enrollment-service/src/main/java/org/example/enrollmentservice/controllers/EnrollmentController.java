package org.example.enrollmentservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.models.Enrollment;
import org.example.enrollmentservice.repostories.EnrollmentRepository;
import org.example.enrollmentservice.requestBodies.EnrollmentRequestBody;
import org.example.enrollmentservice.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {
   private final PaymentService paymentService;
   private final EnrollmentRepository enrollmentRepository;

   @PostMapping("/payment")
    public ResponseEntity<?> payForCourse(@RequestBody @Valid EnrollmentRequestBody requestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
         try {
            return paymentService.payCourses(requestBody);
         }
         catch (Exception e) {
             e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }

   }

    @PostMapping("/students/by-teacher/{teacherId}")
    public List<Long> getStudentsByTeacher(@PathVariable Long teacherId, @RequestBody List<Long> courseIds) {
        return enrollmentRepository.findDistinctStudentIdsByCourseIdIn(courseIds);
    }
}
