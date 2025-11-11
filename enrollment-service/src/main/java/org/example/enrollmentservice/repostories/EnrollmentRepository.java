package org.example.enrollmentservice.repostories;

import org.example.enrollmentservice.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findAllByStudentId(Long studentId);
    List<Long> findDistinctStudentIdsByCourseIdIn(List<Long> courseIds);
}
