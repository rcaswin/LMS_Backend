package com.example.LearningManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorEntity {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private Double rating;

    @ElementCollection
    private List<String> students;  // Now JPA knows how to handle it

    private String bio;
    private String image;
    private String profileLink;
    private String insUID;

    @OneToMany(mappedBy = "instructorEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseEntity> courses;
}
