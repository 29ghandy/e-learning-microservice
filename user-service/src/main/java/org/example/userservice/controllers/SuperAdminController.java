package org.example.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.BulkCreateResponse;
import org.example.userservice.requestBodies.AdminCreateRequest;
import org.example.userservice.requestBodies.BanUserRequest;
import org.example.userservice.requestBodies.BulkCreateUserRequest;
import org.example.userservice.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor

public class SuperAdminController {
    private final AdminService adminService;
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid AdminCreateRequest requestBody) {
        try {
            String message = adminService.createAdmin(requestBody);
            return ResponseEntity.ok().body(message);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ban-admin")
    public ResponseEntity<?> banAdmin(@RequestBody @Valid BanUserRequest requestBody) {
        try {
            String message = adminService.banAdmin(requestBody);
            return ResponseEntity.ok().body(message);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-users")
    public ResponseEntity<?> bulkCreateUsers(@RequestBody List<BulkCreateUserRequest> requestBody) {
        try {
            BulkCreateResponse response = adminService.bulkCreateUsers(requestBody);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
