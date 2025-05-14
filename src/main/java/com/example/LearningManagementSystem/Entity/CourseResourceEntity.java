package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Course_Resources")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // pdf, code, assets, etc.
    private String file;
}

