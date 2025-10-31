package org.example.courseservice.indexies;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard") // Full-text search support
    private String name;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Double)
    private Double discountPercentage;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Double)
    private Double averageRating;

    @Field(type = FieldType.Long)
    private Long teacherId;
}
