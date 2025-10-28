package org.example.courseservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    private Integer number;
    private String answer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "quiz_question_choices",
            joinColumns = @JoinColumn(name = "quiz_question_id"),
            inverseJoinColumns = @JoinColumn(name = "choice_id")
    )
    private Set<Choice> choices = new HashSet<>();
}
