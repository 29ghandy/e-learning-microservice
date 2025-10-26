package org.example.userservice.requestBodies;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BanUserRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private int duration;
    @NotBlank
    private String reason;
}
