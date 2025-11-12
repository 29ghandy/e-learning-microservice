package org.example.courseservice.repositories;

import org.example.courseservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c.id FROM Course c WHERE c.teacherId = :teacherId")
    List<Long> findCourseIdsByTeacherId(Long teacherId);
}
