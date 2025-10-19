package org.example.userservice.repositories;

import org.example.userservice.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
