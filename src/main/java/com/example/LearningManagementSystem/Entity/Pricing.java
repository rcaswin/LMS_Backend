package com.example.LearningManagementSystem.Entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pricing {
    private Long originalPrice;
    private Long discountText;
    private String offerNote;

    @ElementCollection
    private List<String> features;
}

