package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.UserEntity;
import com.example.LearningManagementSystem.Exception.ExceptionHandler;
import com.example.LearningManagementSystem.Model.User;
import com.example.LearningManagementSystem.Repository.UserRepository;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public void signup(User user) {
        List<String> err = new ArrayList<>();

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            err.add("User already exists");
        }
        else {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(user.getName());
            userEntity.setEducation(user.getEducation());
            userEntity.setEmail(user.getEmail());
//            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setDate(user.getDate());
            userEntity.setStatus(user.getStatus());
            userEntity.setUserRole("Student"); //Default user role
            userEntity.setUserUID(user.getUserUID());
            userRepository.save(userEntity);
        }
        if (!err.isEmpty()){
            throw new ExceptionHandler(err, "User already exists");
        }
    }

    public UserEntity login(User user) {
        List<String> err = new ArrayList<>();

        Optional<UserEntity> optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser.isEmpty()) {
            err.add("Invalid Email");
            throw new ExceptionHandler(err, "Invalid Credentials");
        }

        return optionalUser.get();
    }


    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
