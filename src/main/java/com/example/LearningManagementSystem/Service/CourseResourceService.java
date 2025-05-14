package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.CourseResourceEntity;
import com.example.LearningManagementSystem.Repository.CourseResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseResourceService {

    @Autowired
    private CourseResourceRepository resourceRepository;

    public List<CourseResourceEntity> saveAll(List<CourseResourceEntity> resources) {
        return resourceRepository.saveAll(resources);
    }

    public List<CourseResourceEntity> getAll() {
        return resourceRepository.findAll();
    }

    public CourseResourceEntity getById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }
}
