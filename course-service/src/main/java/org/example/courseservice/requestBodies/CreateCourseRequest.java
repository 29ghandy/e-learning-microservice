package org.example.courseservice.requestBodies;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCourseRequest {

   @NotBlank(message = "Course name is required")
   @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Course name must contain only letters, numbers, and spaces")
   private String courseName;

   @NotBlank(message = "Course description is required")
   @Pattern(regexp = "^[A-Za-z0-9 ,.]+$", message = "Description can only contain letters, numbers, spaces, commas, and periods")
   private String courseDescription;

   @NotBlank(message = "Category is required")
   @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Category must contain only letters, numbers, and spaces")
   private String category;

   @NotNull(message = "Course price is required")
   @Positive(message = "Course price must be a positive number")
   private Double coursePrice;

   @NotNull(message = "Number of hours is required")
   @Min(value = 1, message = "Number of hours must be at least 1")
   private Integer numberOfHours;
   private long teacherId;
   private String role;
   private MultipartFile thumbnail;
}
