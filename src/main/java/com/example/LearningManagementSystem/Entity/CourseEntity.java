package com.example.LearningManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    private String id;

    private String title;
    @Column(name = "course_description")
    private String description;
    private String category;
    private String level;
    private String duration;
    private String price;
    private String image;

    private String overviewSubtitle;
    @Column(name = "overview_description")
    private String overviewDescription;

    @ElementCollection
    private List<String> learnItems;

    @Embedded
    private DurationDetails durationDetails;

    @Embedded
    private Pricing pricing;

    @ElementCollection
    private List<String> prerequisites;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<ModuleEntity> modules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CourseResourceEntity> courseResources;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CourseFormatDetail> courseFormatDetails;

    @Embedded
    private FinalAssessment finalAssessment;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonIgnoreProperties("courses")
    private InstructorEntity instructorEntity;

}
