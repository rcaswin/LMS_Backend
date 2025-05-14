package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.DTO.AnnouncementDTO;
import com.example.LearningManagementSystem.Entity.AnnouncementEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Repository.AnnouncementRepository;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    public AnnouncementEntity sendAnnouncement(AnnouncementEntity announcement) {
        // Validate target
        String targetType = announcement.getTargetType();
        String targetId = announcement.getTargetId(); // Now targetId is a String for emails

        if ("STUDENT".equalsIgnoreCase(targetType)) {
            if (targetId == null || !userRepository.existsByEmail(targetId)) {
                throw new IllegalArgumentException("Invalid Student Email: " + targetId);
            }
        } else if ("INSTRUCTOR_STUDENTS".equalsIgnoreCase(targetType)) {
            if (targetId == null || !instructorRepository.existsById(targetId)) {
                throw new IllegalArgumentException("Invalid Instructor ID: " + targetId);
            }
        }

        // Validate sender (Instructor)
        if ("Instructor".equalsIgnoreCase(announcement.getSenderType())) {
            String senderId = announcement.getSenderId();
            if (senderId == null || !instructorRepository.existsById(senderId)) {
                throw new IllegalArgumentException("Invalid Instructor Sender ID: " + senderId);
            }
        }

        // Set the timestamp
        announcement.setTimestamp(LocalDateTime.now());
        return announcementRepository.save(announcement);
    }

    public List<AnnouncementDTO> getAnnouncementsForStudent(String studentEmail) {
        List<AnnouncementEntity> announcements = new ArrayList<>();

        // 1. Fetch student
        UserEntity user = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Announcements for all students
        announcements.addAll(announcementRepository.findByTargetType("ALL"));

        // 3. Personal announcements for this student
        announcements.addAll(announcementRepository.findByTargetTypeAndTargetId("STUDENT", studentEmail));

        // 4. Announcements for instructor's students
        for (String instructorId : user.getInstructorIds()) {
            announcements.addAll(announcementRepository.findByTargetTypeAndTargetId("INSTRUCTOR_STUDENTS", instructorId));
        }

        // 5. Remove deleted announcements
        List<AnnouncementEntity> filtered = announcements.stream()
                .filter(announcement -> !announcement.getDeletedByStudents().contains(studentEmail))
                .collect(Collectors.toList());

        // 6. Convert to DTOs
        List<AnnouncementDTO> announcementDTOs = filtered.stream()
                .map(entity -> new AnnouncementDTO(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getMessage(),
                        entity.getTimestamp(),
                        entity.getAnnounceType(),
                        entity.getSenderType(),
                        entity.getSenderId(),
                        entity.getTargetType(),
                        entity.getTargetId(),
                        entity.getType(),
                        entity.getDeletedByStudents(),
                        entity.getSeenByStudents(),
                        entity.getSeenByStudents().contains(studentEmail) // read status
                ))
                .collect(Collectors.toList());

        return announcementDTOs;
    }




    public void deleteAnnouncementForStudent(Long announcementId, String studentEmail) {
        Optional<AnnouncementEntity> announcementOpt = announcementRepository.findById(announcementId);

        if (!announcementOpt.isPresent()) {
            throw new RuntimeException("Announcement not found");
        }

        AnnouncementEntity announcement = announcementOpt.get();

        // Case 1: If it's a personal announcement for the student
        if ("STUDENT".equals(announcement.getTargetType()) && studentEmail.equals(announcement.getTargetId())) {
            // Delete the announcement for this student (only if it's targeted at them)
            announcementRepository.delete(announcement);
        }
        // Case 2: If it's for all students
        else if ("ALL".equals(announcement.getTargetType())) {
            // Add the student to the deleted list (don't delete the announcement for all)
            if (!announcement.getDeletedByStudents().contains(studentEmail)) {
                announcement.getDeletedByStudents().add(studentEmail);
                announcementRepository.save(announcement);
            }
        }
        // Case 3: If it's for instructor's students
        else if ("INSTRUCTOR_STUDENTS".equals(announcement.getTargetType())) {
            // Log the student's linked instructors
            UserEntity user = userRepository.findByEmail(studentEmail)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            boolean isStudentLinkedToInstructor = false;
            for (String instructorId : user.getInstructorIds()) {
                if (announcement.getTargetId().equals(instructorId)) {
                    isStudentLinkedToInstructor = true;
                    break;
                }
            }

            if (isStudentLinkedToInstructor) {
                // Add the student to the deleted list, but don't delete the announcement itself
                if (!announcement.getDeletedByStudents().contains(studentEmail)) {
                    announcement.getDeletedByStudents().add(studentEmail);
                    announcementRepository.save(announcement);
                }
            } else {
                throw new RuntimeException("Announcement is not targeted at this student");
            }
        } else {
            throw new RuntimeException("Announcement is not targeted at this student");
        }
    }
    public List<AnnouncementEntity> getAnnouncementsBySenderId(String senderId) {
        return announcementRepository.findBySenderId(senderId);
    }
    public boolean deleteAnnouncementById(Long id) {
        if (announcementRepository.existsById(id)) {
            announcementRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
