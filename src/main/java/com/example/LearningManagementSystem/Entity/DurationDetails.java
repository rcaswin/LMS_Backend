package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DurationDetails {
    private Integer totalHours;
    private Integer totalWeeks;
    private String startDate;
    private String endDate;
}

