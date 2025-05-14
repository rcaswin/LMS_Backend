package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Course_Format_Details")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseFormatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String classType;
    private String assesmentTypes; // or rename to 'type' if it's for format
}

