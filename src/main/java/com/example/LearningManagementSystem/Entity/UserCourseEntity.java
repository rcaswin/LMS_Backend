package com.example.LearningManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "User_Enrolled_Courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    private String orderNumber;
    private String purchaseDate;
    private int progress;
    private boolean completed;
    private String action;

    @ElementCollection
    private List<Long> completedModules;

    @ElementCollection
    private List<Long> completedTopics;

    @ElementCollection
    private List<Long> completedQuizzes;

    @ElementCollection
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<Date> attendanceDates;

    private Integer quizAverage = 0;
    private Integer attendanceAverage = 0;

    private boolean courseCompleted = false;

}

