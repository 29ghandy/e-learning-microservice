package org.example.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Users;
import org.example.userservice.requestBodies.SignUpRequest;
import org.example.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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
}
