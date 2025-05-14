package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.UserCourseEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseEntity, Long> {
    List<UserCourseEntity> findByUser(UserEntity user);

    boolean existsByUserAndCourse(UserEntity user, CourseEntity course);
    Optional<UserCourseEntity> findByUserAndCourse(UserEntity user, CourseEntity course);


}
