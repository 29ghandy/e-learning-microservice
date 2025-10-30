package org.example.courseservice.controllers.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Course;
import org.example.courseservice.requestBodies.CreateCourseRequest;
import org.example.courseservice.requestBodies.CreateDiscountRequest;
import org.example.courseservice.requestBodies.DeleteCourseRequest;
import org.example.courseservice.requestBodies.UpdateCourseRequest;
import org.example.courseservice.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course/teacher")
@RequiredArgsConstructor
public class TeacherCourseController {
   private  final  CourseService courseService;

    @PostMapping("/create-course")
    public ResponseEntity<?> createCourse(@ModelAttribute @Valid CreateCourseRequest requestBody, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
             return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
         }
       try {
         return ResponseEntity.ok().body(courseService.addCourse(requestBody));
       }
       catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
   }

    @PutMapping("/update-course")
    public ResponseEntity<?> updateCourse(@ModelAttribute @Valid UpdateCourseRequest requestBody, BindingResult bindingResult) {
         if(bindingResult.hasErrors()) {
             return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
         }
        try {
            return ResponseEntity.ok().body(courseService.updateCourse(requestBody));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete-course")
    public ResponseEntity<?> deleteCourse(@RequestBody DeleteCourseRequest requestBody) {

        try {
            return  ResponseEntity.ok().body(courseService.deleteCourse(requestBody));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/add-discount")
    public ResponseEntity<?> addDiscount(@RequestBody @Valid CreateDiscountRequest requestBody, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok().body(courseService.addDiscount(requestBody));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
