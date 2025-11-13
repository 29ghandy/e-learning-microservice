package org.example.userservice.requestBodies;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AddSocialLinksRequest {
    @NonNull
    @Min(value = 1, message = "teacher id should be at least 1")
    Long teacherId;
    @NonNull
    @NotEmpty
    private List<String> urls;
}
