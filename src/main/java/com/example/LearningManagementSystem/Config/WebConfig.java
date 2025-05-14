package com.example.LearningManagementSystem.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/instructor_images/**", "/course_images/**", "course_resources/**")
                .addResourceLocations("file:instructor_images/", "file:course_images/", "file:course_resources/");
    }
}

