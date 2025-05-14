package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, String> {
    @Query(value = "SELECT id FROM course WHERE id LIKE 'crs%' ORDER BY CAST(SUBSTRING(id, 4) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastCourseId();

    List<CourseEntity> findByInstructorEntityId(String instructorId);

    List<CourseEntity> findByInstructorEntity(InstructorEntity instructor);

}
