package com.example.LearningManagementSystem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {

    private Long id;
    private String title;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM dd, yyyy | HH:mm")
    private LocalDateTime timestamp;

    private String announceType;
    private String senderType;
    private String senderId;
    private String targetType;
    private String targetId;
    private String type;

    private List<String> deletedByStudents;
    private List<String> seenByStudents;

    private boolean read; // true if seen by this student
}
