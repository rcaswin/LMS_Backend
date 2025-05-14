package com.example.LearningManagementSystem.Repository;

import com.example.LearningManagementSystem.Entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    List<AnnouncementEntity> findByTargetType(String targetType);
    List<AnnouncementEntity> findByTargetTypeAndTargetId(String targetType, String targetId);
    List<AnnouncementEntity> findByTargetTypeInAndTargetIdIn(List<String> types, List<String> ids);
    void deleteById(Long id);
    List<AnnouncementEntity> findBySenderId(String senderId);
}
