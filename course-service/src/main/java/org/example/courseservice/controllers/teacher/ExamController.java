package org.example.courseservice.controllers.teacher;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.requestBodies.*;
import org.example.courseservice.services.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/course/teacher/exam")
public class ExamController {
    private final ExamService examService;

    @PostMapping("/create-exam")
    public ResponseEntity<?> createExam(@RequestBody CreateExamRequest request) {
        try {
            return examService.createExam(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-exam")
    public ResponseEntity<?> deleteQuize(@RequestBody DeleteExamRequest request) {
        try {
            return examService.deleteExam(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-exam-questions")
    public ResponseEntity<?> addQuizQuestions(@RequestBody AddExamQuestionsRequest request) {
        try {
            return examService.addExamQuestions(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
