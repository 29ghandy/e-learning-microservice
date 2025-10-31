package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.indexies.CourseIndex;
import org.example.courseservice.models.Course;
import org.example.courseservice.repositories.CourseRepository;
import org.example.courseservice.repositories.CourseSearchRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSyncService {

    private final CourseRepository courseRepository;
    private final CourseSearchRepository courseSearchRepository;


    public void indexCourse(Course course) {
        CourseIndex index = CourseIndex.builder()
                .id(course.getId())
                .name(course.getName())
                .category(course.getCategory())
                .price(course.getPrice())
                .discountPercentage(course.getDiscountPercentage())
                .description(course.getDescription())
                .averageRating(course.getAverageRating())
                .teacherId(course.getTeacherId())
                .build();

        courseSearchRepository.save(index);
    }


    public void deleteCourseIndex(Long courseId) {
        courseSearchRepository.deleteById(courseId.toString());
    }


    public void reindexAll() {
        List<Course> allCourses = courseRepository.findAll();
        List<CourseIndex> indices = allCourses.stream()
                .map(this::mapToIndex)
                .toList();

        courseSearchRepository.saveAll(indices);
    }


    private CourseIndex mapToIndex(Course course) {
        return CourseIndex.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .build();
    }
}
