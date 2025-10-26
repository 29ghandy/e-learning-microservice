package org.example.userservice.requestBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkCreateUserRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}

