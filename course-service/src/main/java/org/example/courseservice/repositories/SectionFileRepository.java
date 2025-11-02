package org.example.courseservice.repositories;

import org.example.courseservice.models.SectionFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionFileRepository extends JpaRepository<SectionFile, Long> {
}
