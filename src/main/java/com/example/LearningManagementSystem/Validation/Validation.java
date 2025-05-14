package com.example.LearningManagementSystem.Validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Validation {
    public static <T> ResponseEntity<?> generate(T data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", data);
        response.put("error", message);
        response.put("status", status.value());
        return new ResponseEntity<>(response, status);
    }
}
