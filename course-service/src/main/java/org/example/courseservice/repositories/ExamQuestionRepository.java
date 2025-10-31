package org.example.courseservice.repositories;

import org.example.courseservice.models.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion,Long> {
}
