package com.example.LearningManagementSystem.DTO;

public class LoginResponseDTO {
    private String username;
    private String email;
    private String userRole;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String username, String email, String userRole) {
        this.username = username;
        this.email = email;
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
