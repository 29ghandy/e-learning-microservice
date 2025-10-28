package org.example.courseservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "choices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String choiceText;

    @ManyToMany(mappedBy = "choices", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<QuizQuestion> quizQuestions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exam_question_choices",
            joinColumns = @JoinColumn(name = "choice_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_question_id")
    )
    @JsonIgnore
    private Set<ExamQuestion> examQuestions = new HashSet<>();
}
