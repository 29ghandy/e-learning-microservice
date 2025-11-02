package org.example.courseservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "section_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @JsonIgnore
    private Section section;

    private Long courseId;
    private String name;
    private String type;
    private String path;
}