package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.CourseResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseResourceRepository extends JpaRepository<CourseResourceEntity, Long> {
}

