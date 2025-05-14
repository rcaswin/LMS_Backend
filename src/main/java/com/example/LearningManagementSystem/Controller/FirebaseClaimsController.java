package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Repository.UserCourseRepository;
import com.example.LearningManagementSystem.Repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/firebase")
@RequiredArgsConstructor
public class FirebaseClaimsController {

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final UserCourseRepository userCourseRepository;

    @PostMapping("/setClaims/{email}")
    public ResponseEntity<String> setClaims(@PathVariable String email) {
        try {
            // 1. Find user or instructor by email
            Optional<UserEntity> userOpt = userRepository.findByEmail(email);
            Optional<InstructorEntity> instructorOpt = instructorRepository.findByEmail(email);

            Map<String, Object> claims = new HashMap<>();

            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();

                // 2. Get enrolled course IDs
                List<String> courseIds = user.getEnrolledCourses().stream()
                        .map(uc -> uc.getCourse().getId())
                        .collect(Collectors.toList());

                claims.put("courses", courseIds);
                claims.put("role", "student");

            } else if (instructorOpt.isPresent()) {
                InstructorEntity instructor = instructorOpt.get();

                List<String> courseIds = instructor.getCourses().stream()
                        .map(CourseEntity::getId)
                        .collect(Collectors.toList());

                claims.put("courses", courseIds);
                claims.put("role", "instructor");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No user or instructor found with email: " + email);
            }

            // 3. Get Firebase UID by email
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

            // 4. Set claims
            FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);

            return ResponseEntity.ok("Custom claims set for: " + email);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}

