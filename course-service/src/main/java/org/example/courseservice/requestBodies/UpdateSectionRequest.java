package org.example.courseservice.requestBodies;

import lombok.Data;

@Data
public class UpdateSectionRequest {
    private Long sectionId;
    private String sectionTitle;
    private Integer sectionNumber;
    private Integer sectionNumberOfHours;
}
