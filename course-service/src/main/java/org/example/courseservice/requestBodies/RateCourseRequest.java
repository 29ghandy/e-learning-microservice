package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;
import org.springframework.jmx.export.annotation.ManagedNotification;

@Data
public class RateCourseRequest {

     @NonNull
     @Min(value = 1 , message =  "course number shouldn't be less than 0" )
     private Long courseId;
     @NonNull
     @Min(value = 0, message = "Numbers of stars should be at least 0")
     private Long numberOfStars;
}
