package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Topics")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class TopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String file;
    private boolean preview;
    private boolean watch;
    private boolean completed = false;
    private boolean hasAssessment = true;
    private LocalDate unlockDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "topic_id")
    private List<MCQQuizEntity> mcqQuizzes;

    public String getUnlockDate() {
        return unlockDate.toString(); // Converts LocalDate to "YYYY-MM-DD"
    }

}


