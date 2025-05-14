package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    // Custom queries (if needed)
}
