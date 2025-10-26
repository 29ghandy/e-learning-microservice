package org.example.userservice.requestBodies;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUpdateUsersRequest {
    String email;
    String name;
    String password;
    String role;
}
