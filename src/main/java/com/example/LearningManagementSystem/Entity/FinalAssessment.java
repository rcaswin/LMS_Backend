package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalAssessment {
    private String type; // e.g., Project, Exam
    private String description;
    private Integer totalMarks;
    private String assessmentDate;
}

