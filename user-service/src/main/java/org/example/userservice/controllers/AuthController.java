package org.example.userservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.requestBodies.HashPasswordRequest;
import org.example.userservice.requestBodies.LoginRequest;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService ;
    @PostMapping("/signup")
    public ResponseEntity<?> signup( @ModelAttribute @Valid SignUpRequest requestBody, BindingResult bindingResult) throws Exception {
       if (bindingResult.hasErrors()) {
           return new ResponseEntity<>( bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
       }
        try {
            String message = authService.signUp(requestBody);
            return ResponseEntity.ok(message);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest requestBody, BindingResult bindingResult, HttpServletResponse response) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>( bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            String message = authService.login(requestBody, response);
            return ResponseEntity.ok(message);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return authService.refreshToken(request, response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
       return authService.logout(request, response);
    }

    @GetMapping("hash-password")
    public String hash(@RequestBody HashPasswordRequest request) {
        return authService.hash(request);
    }
}
