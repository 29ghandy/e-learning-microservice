package org.example.courseservice.services;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Choice;
import org.example.courseservice.models.Quiz;
import org.example.courseservice.models.QuizQuestion;
import org.example.courseservice.models.Section;
import org.example.courseservice.repositories.ChoiceRepository;
import org.example.courseservice.repositories.QuizQuestionRepository;
import org.example.courseservice.repositories.QuizRepository;
import org.example.courseservice.repositories.SectionRepository;
import org.example.courseservice.requestBodies.AddQuizQuestionsRequest;
import org.example.courseservice.requestBodies.CreateQuizRequest;
import org.example.courseservice.requestBodies.DeleteQuizRequest;
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
public class QuizService {
    private final QuizRepository quizRepository;
    private final SectionRepository sectionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final ChoiceRepository choiceRepository;

    public ResponseEntity<?> createQuiz(CreateQuizRequest request) {
        Optional<Section> sec = sectionRepository.findById(request.getSectionId());
        if (sec.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Section not found");
        }

        Section section = sec.get();
        Quiz quiz = new Quiz();
        quiz.setSection(section);
        quiz.setDate(request.getDate());
        quiz.setTitle(request.getTitle());

        quizRepository.save(quiz);

        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    public ResponseEntity<?> deleteQuiz(DeleteQuizRequest request) {
        Optional<Quiz> quiz = quizRepository.findById(request.getQuizId());
        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }

        quizRepository.delete(quiz.get());
        return ResponseEntity.ok().body("Quiz deleted");
    }

    @Transactional
    public ResponseEntity<?> addQuizQuestions(AddQuizQuestionsRequest request) {
        Optional<Quiz> qu = quizRepository.findById(request.getQuizId());
        if (qu.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }

        Quiz quiz =  qu.get();
        // 1️⃣ Collect all choice texts across all questions
        Set<String> allChoiceTexts = request.getQuestions().stream()
                .flatMap(q -> q.getChoices().stream())
                .collect(Collectors.toSet());

        // 2️⃣ Load existing choices
        Map<String, Choice> existingChoices = choiceRepository.findAll().stream()
                .filter(c -> allChoiceTexts.contains(c.getChoiceText()))
                .collect(Collectors.toMap(Choice::getChoiceText, c -> c));

        // 3️⃣ Find which choice texts are new
        List<Choice> newChoices = allChoiceTexts.stream()
                .filter(ct -> !existingChoices.containsKey(ct))
                .map(ct -> Choice.builder().choiceText(ct).build())
                .collect(Collectors.toList());

        // 4️⃣ Save new choices in bulk
        if (!newChoices.isEmpty()) {
            List<Choice> savedChoices = choiceRepository.saveAll(newChoices);
            savedChoices.forEach(c -> existingChoices.put(c.getChoiceText(), c));
        }

        // 5️⃣ Prepare all quiz questions
        List<QuizQuestion> quizQuestions = request.getQuestions().stream()
                .map(q -> {
                    Set<Choice> questionChoices = q.getChoices().stream()
                            .map(existingChoices::get)
                            .collect(Collectors.toSet());

                    return QuizQuestion.builder()
                            .quiz(quiz)
                            .number(q.getNumber())
                            .questionText(q.getQuestionText())
                            .answer(q.getAnswer())
                            .choices(questionChoices)
                            .build();
                })
                .collect(Collectors.toList());

        // 6️⃣ Bulk save all questions
        quizQuestionRepository.saveAll(quizQuestions);

        return ResponseEntity.ok().body(quizQuestions);
    }
}
