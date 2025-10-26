package org.example.userservice.dtos;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class BulkCreateResponse {
    private List<CreatedUserDto> createdUsers = new ArrayList<>();
    private List<FailedUserDto> failedUsers = new ArrayList<>();
}
