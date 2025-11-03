package org.example.courseservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCacheDTO implements Serializable {
    private Double discountPercentage;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private Long discountNumberOfMembers;
}
