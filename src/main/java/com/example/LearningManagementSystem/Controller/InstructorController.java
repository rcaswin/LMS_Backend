package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.DTO.InsLoginResponseDTO;
import com.example.LearningManagementSystem.DTO.InstructorDetailsDTO;
import com.example.LearningManagementSystem.DTO.LoginRequestDTO;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Service.InstructorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/instructors")
@CrossOrigin
public class InstructorController {

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/instructor_images/";

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InstructorRepository instructorRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerInstructor(@RequestBody InstructorEntity instructorEntity) {
        try {
            InstructorEntity savedInstructor = instructorService.saveInstructorBasicDetails(instructorEntity);
            return ResponseEntity.ok(savedInstructor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> instructorLogin(@RequestBody LoginRequestDTO loginRequest) {
        InstructorEntity instructor = instructorService.getInstructorByEmail(loginRequest.getEmail());

        if (instructor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), instructor.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        InsLoginResponseDTO loginResponse = new InsLoginResponseDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail()
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateInstructorProfile(
            @PathVariable String id,
            @RequestPart("profileData") String profileData,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            InstructorEntity updatedData = objectMapper.readValue(profileData, InstructorEntity.class);

            String uploadedImageName = null;
            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(UPLOAD_DIR));

                String filename = StringUtils.cleanPath(image.getOriginalFilename());
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                uploadedImageName = filename;
            }

            InstructorEntity updatedInstructor = instructorService.updateInstructorProfileWithImage(id, updatedData, uploadedImageName);

            if (updatedInstructor != null) {
                return ResponseEntity.ok(updatedInstructor);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<InstructorEntity> getInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable String id) {
        InstructorEntity instructor = instructorService.getInstructorById(id);
        if (instructor != null) {
            return ResponseEntity.ok(instructor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with ID: " + id);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInstructor(@PathVariable String id) {
        boolean isDeleted = instructorService.deleteInstructorById(id);

        if (isDeleted) {
            return ResponseEntity.ok("Instructor deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found");
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<?> getInstructorByEmail(@RequestParam("email") String email) {
        try {
            InstructorEntity instructor = instructorService.getInstructorByEmail(email);
            return ResponseEntity.ok(instructor);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getInstructorDetails(@RequestParam("email") String email) {
        try {
            InstructorDetailsDTO details = instructorService.getInstructorDetailsByEmail(email);
            return ResponseEntity.ok(details);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Or use a logger here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<String>> getStudentsByInstructorId(@PathVariable String id) {
        return instructorRepository.findById(id)
                .map(instructor -> ResponseEntity.ok(instructor.getStudents()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<InstructorEntity> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteInstructorById(@PathVariable String id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
            return "Instructor with ID " + id + " deleted successfully.";
        } else {
            return "Instructor with ID " + id + " not found.";
        }
    }

}
