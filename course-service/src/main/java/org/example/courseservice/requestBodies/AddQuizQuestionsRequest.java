package org.example.courseservice.requestBodies;

import lombok.Data;

import java.util.List;

@Data
public class AddQuizQuestionsRequest {
    private Long quizId;
    private List<QuizQuestionRequest> questions;

    @Data
    public static class QuizQuestionRequest {
        private Integer number;
        private String questionText;
        private String answer;
        private List<String> choices;
    }
}
