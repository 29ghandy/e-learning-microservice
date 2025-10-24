package org.example.userservice.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.requestBodies.ChangePasswordRequest;
import org.example.userservice.requestBodies.ForgetPasswordRequest;
import org.example.userservice.requestBodies.ResetPasswordRequest;
import org.example.userservice.services.PasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest requestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {

            return ResponseEntity.ok().body(passwordService.changePassword(requestBody));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody @Valid ForgetPasswordRequest requestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok().body(passwordService.forgetPassword(requestBody.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest requestBody,BindingResult bindingResult ){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {


            return ResponseEntity.ok().body( passwordService.resetPassword(requestBody));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
