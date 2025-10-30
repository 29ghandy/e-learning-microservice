package org.example.courseservice.repositories;

import org.example.courseservice.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section,Long> {

}
