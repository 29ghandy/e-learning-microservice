package org.example.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedUserDto {
    private String email;
    private String reason;
}
