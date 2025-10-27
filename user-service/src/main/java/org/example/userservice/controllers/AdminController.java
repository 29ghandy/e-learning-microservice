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

    @PutMapping("/update-users")
    public ResponseEntity<?> bulkUpdateUsers(@RequestBody List<BulkUpdateUsersRequest> requestBody) {
        try {
            BulkUpdateResponse response = adminService.bulkUpdateUsers(requestBody);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/ban-users")
    public ResponseEntity<?> bulkBanUsers(@RequestBody List<BulkBanRequest> requestBody) {
        try {
            BulkBanResponse response = adminService.bulkBanUsers(requestBody);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
