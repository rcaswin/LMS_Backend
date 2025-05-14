package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Quiz")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MCQQuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String[] options; // For MCQ options
    private String correctAnswer;
    private Integer marks;
}

