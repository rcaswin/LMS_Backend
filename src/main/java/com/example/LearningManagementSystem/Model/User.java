package com.example.LearningManagementSystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String email;
    private String education;
    private String userRole;
    private String date;
    private String status;
    private String userUID;
}