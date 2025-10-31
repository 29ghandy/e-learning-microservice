package org.example.courseservice.controllers.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.courseservice.models.Section;
import org.example.courseservice.requestBodies.UploadFileRequest;
import org.example.courseservice.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/course/teacher/upload")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/chunk")
    public ResponseEntity<?> addFile(@ModelAttribute @Valid UploadFileRequest requestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            return fileService.addFile(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-file")
    public ResponseEntity<?> updateFile(@RequestBody Section section) {
        return null;
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<?> removeFiles(@RequestBody Section section) {
        return null;
    }
}