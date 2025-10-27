package org.example.courseservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Course {
    @Id
    long id;

    String name;
    String description;

}
