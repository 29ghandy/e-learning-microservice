package org.example.courseservice.controllers.teacher;

import org.example.courseservice.models.Section;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course/teacher")
public class SectionController {
    @PostMapping("/create-section")
    public ResponseEntity<?> createSection(@RequestBody Section section) {
         return null;
    }
    @PutMapping("/update-section")
    public ResponseEntity<?> updateSection(@RequestBody Section section) {
        return null;
    }
    @DeleteMapping("/delete-section")
    public ResponseEntity<?> deleteSection(@RequestBody Section section) {
        return null;
    }
    @PostMapping("/add-files") // bulk
    public ResponseEntity<?> addFiles(@RequestBody Section section) {
              return null;
    }
    @PostMapping("/update-file")
    public ResponseEntity<?> updateFile(@RequestBody Section section) {
        return null;
    }
    @DeleteMapping("/delete-files") // bulk
    public ResponseEntity<?> removeFiles(@RequestBody Section section) {
        return null;
    }
}
