package org.example.courseservice.repositories;

import org.example.courseservice.models.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    Optional<Choice> findByChoiceText(String choiceText);
}
