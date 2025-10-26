package org.example.userservice.requestBodies;

import lombok.Data;

@Data
public class AdminCreateRequest {
    String name;
    String email;
    String password;
}
