package org.example.courseservice.controllers.teacher;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Quiz;
import org.example.courseservice.requestBodies.AddQuizQuestionsRequest;
import org.example.courseservice.requestBodies.CreateQuizRequest;
import org.example.courseservice.requestBodies.DeleteQuizRequest;
import org.example.courseservice.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/course/teacher/quiz")
public class QuizController {
    private final QuizService quizService;

    @PostMapping("/create-quiz")
    public ResponseEntity<?> createQuize(@RequestBody CreateQuizRequest request) {
        try {
            return quizService.createQuiz(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-quiz")
    public ResponseEntity<?> deleteQuize(@RequestBody DeleteQuizRequest request) {
        try {
            return quizService.deleteQuiz(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-quiz-questions")
    public ResponseEntity<?> addQuizQuestions(@RequestBody AddQuizQuestionsRequest request) {
        try {
            return quizService.addQuizQuestions(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
