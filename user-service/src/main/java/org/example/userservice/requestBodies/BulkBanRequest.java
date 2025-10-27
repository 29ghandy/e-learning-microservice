package org.example.userservice.requestBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkBanRequest {
    private String email;
    private String reason;
    private Integer durationDays;
}
