package com.example.LearningManagementSystem.Exception;

import java.util.List;

public class ExceptionHandler extends RuntimeException{
    List<String> error;
    String message;

    public ExceptionHandler(List<String> error, String message) {
        this.error = error;
        this.message = message;
    }

    public List<String> getError() {
        return error;
    }
}
