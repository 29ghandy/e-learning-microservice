package org.example.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BanSuccessDto {
    private Long id;
    private String email;
    private LocalDateTime banExpiresAt;
    private String message;
    private String reason;
    private Integer durationDays;
}
