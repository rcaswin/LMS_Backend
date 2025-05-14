package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.DTO.InstructorDetailsDTO;
import com.example.LearningManagementSystem.Entity.CourseEntity;
import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Repository.CourseRepository;
import com.example.LearningManagementSystem.Repository.InstructorRepository;
import com.example.LearningManagementSystem.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public InstructorEntity saveInstructorBasicDetails(InstructorEntity instructorEntity) {
        String nextId = generateNextTeacherId();
        instructorEntity.setId(nextId);

        String plainPassword = instructorEntity.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        instructorEntity.setPassword(hashedPassword);

        // Ensure only name, email and password are stored initially
        instructorEntity.setBio(null);
        instructorEntity.setImage(null);
        instructorEntity.setProfileLink(null);
        instructorEntity.setRating(null);
        instructorEntity.setStudents(new ArrayList<>());

        return instructorRepository.save(instructorEntity);
    }

    public InstructorEntity getInstructorByEmail(String email) {
        Optional<InstructorEntity> optionalInstructorEntity= instructorRepository.findByEmail(email);
        return optionalInstructorEntity.orElse(null);
    }


    public List<InstructorEntity> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public InstructorEntity saveInstructorDetails(InstructorEntity instructorEntity) {
        String nextId = generateNextTeacherId();
        instructorEntity.setId(nextId);

        String plainPassword = instructorEntity.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        instructorEntity.setPassword(hashedPassword);

        return instructorRepository.save(instructorEntity);
    }

    public InstructorEntity updateInstructorProfileWithImage(String id, InstructorEntity updatedData, String imageName) {
        Optional<InstructorEntity> existingInstructorOpt = instructorRepository.findById(id);

        if (existingInstructorOpt.isPresent()) {
            InstructorEntity existingInstructor = existingInstructorOpt.get();

            // Update fields
            existingInstructor.setBio(updatedData.getBio());
            existingInstructor.setProfileLink(updatedData.getProfileLink());
            existingInstructor.setRating(updatedData.getRating());

            if (imageName != null) {
                existingInstructor.setImage(imageName);
            }

            return instructorRepository.save(existingInstructor);
        }
        return null;
    }

    public InstructorEntity getInstructorById(String id) {
        return instructorRepository.findById(id).orElse(null);
    }

    private String generateNextTeacherId() {
        String lastId = instructorRepository.findLastTeacherId(); // e.g., "trs12"
        int nextNumber = 1;

        if (lastId != null && lastId.startsWith("trs")) {
            try {
                String numberPart = lastId.substring(3); // extract number after "trs"
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // In case the lastId is something like "trsX" or malformed
                nextNumber = 1;
            }
        }

        return "trs" + nextNumber;
    }

    public boolean deleteInstructorById(String id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public InstructorEntity updateInstructor(String id, InstructorEntity updatedInstructor) {
        if (instructorRepository.existsById(id)) {
            updatedInstructor.setId(id);
            return instructorRepository.save(updatedInstructor);
        }
        return null; // Or throw an exception
    }

    public InstructorDetailsDTO getInstructorDetailsByEmail(String email) {
        InstructorEntity instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found with email: " + email));

        List<UserEntity> students = userRepository.findByEmailIn(instructor.getStudents());
        List<CourseEntity> courseEntities = courseRepository.findByInstructorEntity(instructor);

        return new InstructorDetailsDTO(instructor, students, courseEntities);
    }
}
