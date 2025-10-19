package org.example.userservice.repositories;


import org.example.userservice.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Long> {
}
