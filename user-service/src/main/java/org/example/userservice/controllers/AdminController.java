package org.example.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.BulkBanResponse;
import org.example.userservice.dtos.BulkCreateResponse;
import org.example.userservice.dtos.BulkUpdateResponse;
import org.example.userservice.requestBodies.*;
import org.example.userservice.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
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

    @PutMapping("/update-users")
    public ResponseEntity<?> bulkUpdateUsers(@RequestBody List<BulkUpdateUsersRequest> requestBody) {
        try {
            BulkUpdateResponse response = adminService.bulkUpdateUsers(requestBody);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/ban-users")
    public ResponseEntity<?> bulkBanUsers(@RequestBody List<BulkBanRequest> requestBody) {
        try {
            BulkBanResponse response = adminService.bulkBanUsers(requestBody);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
