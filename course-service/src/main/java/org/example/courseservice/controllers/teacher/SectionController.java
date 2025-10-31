package org.example.courseservice.controllers.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Section;
import org.example.courseservice.requestBodies.CreateSectionRequest;
import org.example.courseservice.requestBodies.DeleteSectionRequest;
import org.example.courseservice.requestBodies.UpdateSectionRequest;
import org.example.courseservice.services.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/course/teacher/section")
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/create-section")
    public ResponseEntity<?> createSection(@RequestBody @Valid CreateSectionRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        try{
            return sectionService.createSection(request);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-section")
    public ResponseEntity<?> updateSection(@RequestBody UpdateSectionRequest request) {
        try {
            return sectionService.updateSection(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-section")
    public ResponseEntity<?> deleteSection(@RequestBody DeleteSectionRequest request) {
        try {
            return sectionService.deleteSection(request);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
