package org.example.enrollmentservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.models.Enrollment;
import org.example.enrollmentservice.requestBodies.EnrollmentRequestBody;
import org.example.enrollmentservice.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {
  private final PaymentService paymentService;
   @PostMapping("/payment")
    public ResponseEntity<?> payForCourse(@RequestBody EnrollmentRequestBody requestBody) {

         try {
            return paymentService.payCourses(requestBody);
         }
         catch (Exception e) {
             e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }

   }
}
