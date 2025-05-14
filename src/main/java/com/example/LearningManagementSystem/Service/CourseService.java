package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Repository.CourseRepository;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Repository.ModuleRepository;
import com.example.LearningManagementSystem.Repository.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Transactional
    public CourseEntity saveCourse(CourseEntity courseEntity) {
        if (courseEntity.getInstructorEntity() != null && courseEntity.getInstructorEntity().getId() != null) {
            InstructorEntity instructor = instructorRepository
                    .findById(courseEntity.getInstructorEntity().getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            courseEntity.setInstructorEntity(instructor);
        }
        String nextId = generateNextCourseId();
        courseEntity.setId(nextId);
        return courseRepository.save(courseEntity);
    }

    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }

    public CourseEntity getCourseById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    private String generateNextCourseId() {
        String lastId = courseRepository.findLastCourseId(); // e.g., "trs12"
        int nextNumber = 1;

        if (lastId != null && lastId.startsWith("crs")) {
            try {
                String numberPart = lastId.substring(3); // extract number after "trs"
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // In case the lastId is something like "trsX" or malformed
                nextNumber = 1;
            }
        }

        return "crs" + nextNumber;
    }

    @Transactional
    public CourseEntity updateCourse(String id, CourseEntity updatedCourse) {
        if (courseRepository.existsById(id)) {
            updatedCourse.setId(id);
            return courseRepository.save(updatedCourse);
        }
        return null; // Or throw an exception
    }
    public List<CourseEntity> getCoursesByInstructorId(String instructorId) {
        return courseRepository.findByInstructorEntityId(instructorId);
    }
}
