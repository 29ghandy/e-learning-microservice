package org.example.enrollmentservice.repostories;

import org.example.enrollmentservice.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findAllByStudentId(Long studentId);
    @Query("SELECT DISTINCT e.studentId FROM Enrollment e WHERE e.courseId IN :courseIds")
    List<Long> findDistinctStudentIdsByCourseIdIn(@Param("courseIds") List<Long> courseIds);
}
