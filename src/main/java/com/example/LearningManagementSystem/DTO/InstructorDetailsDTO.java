package com.example.LearningManagementSystem.DTO;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class InstructorDetailsDTO {
    private String id;
    private String name;
    private String email;
    private Double rating;
    private String bio;
    private String image;
    private String profileLink;

    private List<UserEntity> students;
    private List<CourseEntity> courses;

    public InstructorDetailsDTO(InstructorEntity instructor, List<UserEntity> students, List<CourseEntity> courses) {
        this.id = instructor.getId();
        this.name = instructor.getName();
        this.email = instructor.getEmail();
        this.rating = instructor.getRating();
        this.bio = instructor.getBio();
        this.image = instructor.getImage();
        this.profileLink = instructor.getProfileLink();
        this.students = students;
        this.courses = courses;
    }
}

