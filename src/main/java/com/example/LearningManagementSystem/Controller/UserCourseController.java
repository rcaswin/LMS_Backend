package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.UserCourseEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Repository.CourseRepository;
import com.example.LearningManagementSystem.Repository.UserCourseRepository;
import com.example.LearningManagementSystem.Repository.UserRepository;
import com.example.LearningManagementSystem.Service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-course")
@CrossOrigin("*")
public class UserCourseController {

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    // GET courses by user email
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserCourses(@PathVariable String email) {
        try {
            List<UserCourseEntity> courses = userCourseService.getCoursesByUserEmail(email);
            if (courses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No enrolled courses found for email: " + email);
            }
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving courses: " + e.getMessage());
        }
    }

    // POST enroll course
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollCourse(@RequestBody UserCourseEntity userCourseEntity) {
        try {
            UserCourseEntity saved = userCourseService.saveEnrollment(userCourseEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to enroll: " + e.getMessage());
        }
    }

    @PostMapping("/add-attendance")
    public ResponseEntity<String> addAttendanceDate(
            @RequestParam String userEmail,
            @RequestParam String courseId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date attendanceDate,
            @RequestParam Integer attendanceAverage) {

        Optional<UserEntity> userOpt = userRepository.findByEmail(userEmail);
        Optional<CourseEntity> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or course ID.");
        }

        Optional<UserCourseEntity> userCourseOpt = userCourseRepository.findByUserAndCourse(userOpt.get(), courseOpt.get());

        if (userCourseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not enrolled in this course.");
        }

        UserCourseEntity userCourse = userCourseOpt.get();

        List<Date> attendanceDates = userCourse.getAttendanceDates();
        if (attendanceDates == null) {
            attendanceDates = new ArrayList<>();
        }

        if (!attendanceDates.contains(attendanceDate)) {
            attendanceDates.add(attendanceDate);
            userCourse.setAttendanceDates(attendanceDates);
        }

        // Update attendance average received from frontend
        userCourse.setAttendanceAverage(attendanceAverage);

        userCourseRepository.save(userCourse);

        return ResponseEntity.ok("Attendance and average updated successfully.");
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserCourseDetails(
            @RequestParam String userEmail,
            @RequestParam String courseId) {

        return userCourseService.getUserCourseDetails(userEmail, courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

}
