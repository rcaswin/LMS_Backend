package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<InstructorEntity, String> {

    @Query(value = "SELECT id FROM instructor WHERE id LIKE 'trs%' ORDER BY CAST(SUBSTRING(id, 4) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastTeacherId();

    Optional<InstructorEntity> findByEmail(String email);

}
