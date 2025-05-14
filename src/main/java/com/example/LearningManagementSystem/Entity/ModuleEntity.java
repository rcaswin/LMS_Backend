package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Module_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String lessons;
    private String hours;
    private Boolean completed = false;

//    private String assessmentType; // e.g., MCQ, Project, Assignment
//    private Integer maxMarks; // For storing the maximum marks for the module
//    private String description; // Brief description about the assessment
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "module_id")
    private List<TopicEntity> topics = new ArrayList<>();
}

