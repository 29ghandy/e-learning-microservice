package org.example.courseservice.services;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.indexies.CourseIndex;
import org.example.courseservice.models.Course;
import org.example.courseservice.repositories.CourseRepository;
import org.example.courseservice.repositories.CourseSearchRepository;
import org.example.courseservice.requestBodies.CreateCourseRequest;
import org.example.courseservice.requestBodies.CreateDiscountRequest;
import org.example.courseservice.requestBodies.DeleteCourseRequest;
import org.example.courseservice.requestBodies.UpdateCourseRequest;
import org.example.courseservice.services.helper.CourseSyncService;
import org.example.courseservice.services.helper.ImageSaver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseSearchRepository courseSearchRepository;
    private final CourseSyncService courseSyncService;
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getTeacherCourses(int id) {
        return courseRepository.findAll();
    }

    public Course addCourse(CreateCourseRequest request) throws IOException {
        Course course = new Course();
        course.setName(request.getCourseName());
        course.setCategory(request.getCategory());
        course.setDiscountPercentage(0.0);
        course.setPrice(request.getCoursePrice());
        course.setNumberOfHours(request.getNumberOfHours());
        String path = ImageSaver.saveImage(request.getThumbnail());
        course.setThumbnailPath(path);
        course.setTeacherId(request.getTeacherId());
        course.setAverageRating(5.0);
        course.setStartDate(LocalDateTime.now());
        course.setEndDate(LocalDateTime.now().plusDays(1));
        courseRepository.save(course);
        courseSyncService.indexCourse(course);
        return course;
    }

    public String updateCourse(UpdateCourseRequest request) throws Exception {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new Exception("No such course"));
        if (!course.getTeacherId().equals(request.getTeacherId())) {
            if (request.getTeacherId() > 0) {
                throw new Exception("teacher id mismatch");
            }
        }
        if (request.getCourseName() != null)
            course.setName(request.getCourseName());
        if (request.getCategory() != null)
            course.setCategory(request.getCategory());
        if (request.getCoursePrice() != null)
            course.setPrice(request.getCoursePrice());
        if (request.getNumberOfHours() != null)
            course.setNumberOfHours(request.getNumberOfHours());
        if (request.getThumbnail() != null) {
            String path = ImageSaver.saveImage(request.getThumbnail());
            course.setThumbnailPath(path);
        }
       courseRepository.save(course);
      courseSyncService.indexCourse(course);

        return "course updated";
    }

    public String deleteCourse(DeleteCourseRequest request) throws Exception {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new Exception("no such course"));
        if (!course.getTeacherId().equals(request.getTeacherId())) {
            if (request.getTeacherId() > 0) {
                throw new Exception("teacher id mismatch");
            }
        }
        courseRepository.delete(course);
        return "course deleted";
    }
    public String addDiscount(CreateDiscountRequest request) throws Exception {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new Exception("no such course"));
        course.setDiscountPercentage(request.getDiscountPercentage());
        course.setPrice(course.getPrice() - course.getPrice() * request.getDiscountPercentage() * 0.01);
        course.setStartDate(LocalDateTime.now());
        course.setEndDate(LocalDateTime.now().plusDays(request.getNumberOfDays()));
        courseRepository.save(course);
        return "discount added";
    }
    public List<CourseIndex> findByNameContainingIgnoreCase(String name){
        return courseSearchRepository.findByNameContainingIgnoreCase(name);
    }
}
