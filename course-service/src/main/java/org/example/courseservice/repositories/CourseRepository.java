package org.example.courseservice.repositories;

import org.example.courseservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Long> findCourseIdsByTeacherId(Long teacherId);
}
