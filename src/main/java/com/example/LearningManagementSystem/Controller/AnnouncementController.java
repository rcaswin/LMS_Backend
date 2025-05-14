package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.DTO.AnnouncementDTO;
import com.example.LearningManagementSystem.Entity.AnnouncementEntity;
import com.example.LearningManagementSystem.Repository.AnnouncementRepository;
import com.example.LearningManagementSystem.Service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    @Autowired
    private final AnnouncementService announcementService;

    @Autowired
    private final AnnouncementRepository announcementRepository;

    @PostMapping("/send")
    public AnnouncementEntity sendAnnouncement(@RequestBody AnnouncementEntity announcement) {
        return announcementService.sendAnnouncement(announcement);
    }

    @GetMapping("/student/{studentEmail}")
    public List<AnnouncementDTO> getStudentAnnouncements(@PathVariable String studentEmail) {
        return announcementService.getAnnouncementsForStudent(studentEmail);
    }

    @DeleteMapping("/announcement/{announcementId}/student/{studentEmail}")
    public ResponseEntity<?> deleteAnnouncementForStudent(@PathVariable Long announcementId,
                                                          @PathVariable String studentEmail) {
        try {
            announcementService.deleteAnnouncementForStudent(announcementId, studentEmail);
            return ResponseEntity.ok().body("Announcement deleted for student.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete: " + e.getMessage());
        }
    }

    @PutMapping("/{announcementId}/mark-read/{studentEmail}")
    public ResponseEntity<String> markAsRead(@PathVariable Long announcementId,
                                             @PathVariable String studentEmail) {
        Optional<AnnouncementEntity> optional = announcementRepository.findById(announcementId);

        if (!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Announcement not found");
        }

        AnnouncementEntity announcement = optional.get();

        if (!announcement.getSeenByStudents().contains(studentEmail)) {
            announcement.getSeenByStudents().add(studentEmail);
            announcementRepository.save(announcement);
        }
        return ResponseEntity.ok("Marked as read");
    }
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<AnnouncementEntity>> getAnnouncementsBySenderId(@PathVariable String senderId) {
        List<AnnouncementEntity> announcements = announcementService.getAnnouncementsBySenderId(senderId);
        return ResponseEntity.ok(announcements);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        boolean deleted = announcementService.deleteAnnouncementById(id);
        if (deleted) {
            return ResponseEntity.ok("Announcement deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Announcement not found with ID: " + id);
        }
    }
}
