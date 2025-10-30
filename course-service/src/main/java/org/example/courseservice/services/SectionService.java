package org.example.courseservice.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Course;
import org.example.courseservice.models.Section;
import org.example.courseservice.repositories.CourseRepository;
import org.example.courseservice.repositories.SectionRepository;
import org.example.courseservice.requestBodies.CreateSectionRequest;
import org.example.courseservice.requestBodies.DeleteSectionRequest;
import org.example.courseservice.requestBodies.UpdateSectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final CourseRepository  courseRepository;

    public ResponseEntity<?> createSection(@RequestBody @Valid CreateSectionRequest request) {
        Optional<Course> course = courseRepository.findById(request.getCourseId());
        if (!course.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        Section section = new Section();
        section.setCourse(course.get());
        section.setNumber(request.getSectionNumber());
        section.setNumberOfHours(request.getSectionNumberOfHours());
        section.setTitle(request.getSectionTitle());

        sectionRepository.save(section);
        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }

    public ResponseEntity<?> updateSection(@RequestBody UpdateSectionRequest request) {
        Optional<Section> section = sectionRepository.findById(request.getSectionId());
        if (!section.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Section Not Found");
        }

        Section sec =  section.get();
        if (request.getSectionNumber() != null) {
            sec.setNumber(request.getSectionNumber());
        }
        if (request.getSectionNumberOfHours() != null) {
            sec.setNumberOfHours(request.getSectionNumberOfHours());
        }
        if (request.getSectionTitle() != null) {
            sec.setTitle(request.getSectionTitle());
        }

        sectionRepository.save(sec);
        return ResponseEntity.status(HttpStatus.OK).body(sec);
    }

    public ResponseEntity<?> deleteSection(@RequestBody DeleteSectionRequest request) {
        Optional<Section> section = sectionRepository.findById(request.getSectionId());
        if (!section.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Section Not Found");
        }

        sectionRepository.delete(section.get());
        return ResponseEntity.status(HttpStatus.OK).body(section);
    }
}
