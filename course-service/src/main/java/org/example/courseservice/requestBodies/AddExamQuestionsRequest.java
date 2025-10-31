package org.example.courseservice.requestBodies;

import lombok.Data;
import java.util.List;

@Data
public class AddExamQuestionsRequest {
    private Long examId;
    private List<ExamQuestionRequest> questions;

    @Data
    public static class ExamQuestionRequest {
        private String questionText;
        private Integer number;
        private String answer;
        private List<String> choices;
    }
}
