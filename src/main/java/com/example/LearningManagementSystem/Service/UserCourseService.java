package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.UserCourseEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Repository.CourseRepository;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Repository.UserCourseRepository;
import com.example.LearningManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserCourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    public UserCourseEntity saveEnrollment(UserCourseEntity userCourseEntity) {
        String userEmail = userCourseEntity.getUser().getEmail();
        String courseId = userCourseEntity.getCourse().getId();

        // Fetch user and course
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if already enrolled (optional)
        boolean alreadyEnrolled = userCourseRepository.existsByUserAndCourse(user, course);
        if (alreadyEnrolled) {
            throw new RuntimeException("User already enrolled in this course");
        }

        // Save enrollment
        UserCourseEntity enrollment = new UserCourseEntity();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setOrderNumber(userCourseEntity.getOrderNumber());
        enrollment.setPurchaseDate(userCourseEntity.getPurchaseDate());
        enrollment.setProgress(userCourseEntity.getProgress());
        enrollment.setCompleted(userCourseEntity.isCompleted());
        enrollment.setAction(userCourseEntity.getAction());

        // Add user to instructor's student list
        InstructorEntity instructor = course.getInstructorEntity();
        if (instructor != null) {
            String studentInfo = user.getEmail();
            List<String> studentList = instructor.getStudents();
            if (!studentList.contains(studentInfo)) {
                studentList.add(studentInfo);
                instructor.setStudents(studentList);
                instructorRepository.save(instructor);
            }
            List<String> instructorIds = user.getInstructorIds();
            if (!instructorIds.contains(instructor.getId())) {
                instructorIds.add(instructor.getId());
                user.setInstructorIds(instructorIds);
                userRepository.save(user); // Save the updated user
            }
        }
        return userCourseRepository.save(enrollment);
    }

    public List<UserCourseEntity> getCoursesByUserEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return userCourseRepository.findByUser(user);
        }
        return null;
    }

    public Optional<UserCourseEntity> getUserCourseDetails(String userEmail, String courseId) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(userEmail);
        Optional<CourseEntity> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            return userCourseRepository.findByUserAndCourse(userOpt.get(), courseOpt.get());
        }

        return Optional.empty();
    }
}
