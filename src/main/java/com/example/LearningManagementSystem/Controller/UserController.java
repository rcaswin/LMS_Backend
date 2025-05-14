package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.DTO.LoginResponseDTO;
import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Exception.ExceptionHandler;
import com.example.LearningManagementSystem.Model.User;
import com.example.LearningManagementSystem.Repository.UserRepository;
import com.example.LearningManagementSystem.Service.UserService;
import com.example.LearningManagementSystem.Validation.Validation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/registration")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
    try{
        userService.signup(user);
        return Validation.generate("Registered Successfully", null, HttpStatus.OK);
    }catch (ExceptionHandler exceptionHandler){
        return Validation.generate(exceptionHandler.getError(), "Registration failed", HttpStatus.BAD_REQUEST);
    }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            UserEntity userEntity = userService.login(user);

            return ResponseEntity.ok(userEntity);
        } catch (ExceptionHandler exceptionHandler) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials");
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<UserEntity> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User with ID " + id + " deleted successfully.";
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable Long id,
            @RequestBody User updatedUserData
    ) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUserData.getName());
                    user.setEmail(updatedUserData.getEmail());
                    user.setStatus(updatedUserData.getStatus());
                    user.setEducation(updatedUserData.getEducation());
                    userRepository.save(user);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
