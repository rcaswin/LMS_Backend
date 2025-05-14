package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.MCQQuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCQQuizRepository extends JpaRepository<MCQQuizEntity, Long> {
    // Custom queries (if needed)
}

