package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
    // Custom queries (if needed)
}

