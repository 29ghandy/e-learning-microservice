package org.example.courseservice.services;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.*;
import org.example.courseservice.repositories.*;
import org.example.courseservice.requestBodies.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ChoiceRepository choiceRepository;

    public ResponseEntity<?> createExam(CreateExamRequest request) {
        Optional<Course> c = courseRepository.findById(request.getCourseId());
        if (c.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Section not found");
        }

        Course course = c.get();
        Exam exam = new Exam();
        exam.setCourse(course);
        exam.setDate(request.getDate());
        exam.setTitle(request.getTitle());

        examRepository.save(exam);

        return ResponseEntity.status(HttpStatus.CREATED).body(exam);
    }

    public ResponseEntity<?> deleteExam(DeleteExamRequest request) {
        Optional<Exam> exam = examRepository.findById(request.getExamId());
        if (exam.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }

        examRepository.delete(exam.get());
        return ResponseEntity.ok().body("Quiz deleted");
    }

    @Transactional
    public ResponseEntity<?> addExamQuestions(AddExamQuestionsRequest request) {
        // 1️⃣ Check if exam exists
        Optional<Exam> ex = examRepository.findById(request.getExamId());
        if (ex.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found");
        }

        Exam exam = ex.get();

        // 2️⃣ Collect all choice texts across all exam questions
        Set<String> allChoiceTexts = request.getQuestions().stream()
                .flatMap(q -> q.getChoices().stream())
                .collect(Collectors.toSet());

        // 3️⃣ Load existing choices from DB
        Map<String, Choice> existingChoices = choiceRepository.findAll().stream()
                .filter(c -> allChoiceTexts.contains(c.getChoiceText()))
                .collect(Collectors.toMap(Choice::getChoiceText, c -> c));

        // 4️⃣ Find new choices
        List<Choice> newChoices = allChoiceTexts.stream()
                .filter(ct -> !existingChoices.containsKey(ct))
                .map(ct -> Choice.builder().choiceText(ct).build())
                .collect(Collectors.toList());

        // 5️⃣ Bulk save new choices
        if (!newChoices.isEmpty()) {
            List<Choice> savedChoices = choiceRepository.saveAll(newChoices);
            savedChoices.forEach(c -> existingChoices.put(c.getChoiceText(), c));
        }

        // 6️⃣ Prepare exam questions
        List<ExamQuestion> examQuestions = request.getQuestions().stream()
                .map(q -> {
                    Set<Choice> questionChoices = q.getChoices().stream()
                            .map(existingChoices::get)
                            .collect(Collectors.toSet());

                    return ExamQuestion.builder()
                            .exam(exam)
                            .number(q.getNumber())
                            .questionText(q.getQuestionText())
                            .answer(q.getAnswer())
                            .choices(questionChoices)
                            .build();
                })
                .collect(Collectors.toList());

        // 7️⃣ Bulk save exam questions
        examQuestionRepository.saveAll(examQuestions);

        // 8️⃣ Return success
        return ResponseEntity.status(HttpStatus.CREATED).body(examQuestions);
    }
}