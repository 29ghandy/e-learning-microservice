package org.example.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatedUserDto {
    private Long id;
    private String email;
    private String name;
}

