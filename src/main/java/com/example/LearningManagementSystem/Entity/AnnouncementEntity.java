package com.example.LearningManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM dd, yyyy | HH:mm")
    private LocalDateTime timestamp;

    private String announceType;

    private String senderType; // "Instructor" or "Admin"

    private String senderId;   // Instructor or Admin ID

    private String targetType; // "ALL", "STUDENT", "INSTRUCTOR_STUDENTS"

    private String targetId;   // Null if ALL, or Student ID or Instructor ID

    private String type;

    @ElementCollection
    private List<String> deletedByStudents;

    @ElementCollection
    private List<String> seenByStudents;

}

