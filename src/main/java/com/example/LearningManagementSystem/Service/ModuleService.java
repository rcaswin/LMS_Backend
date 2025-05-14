package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.ModuleEntity;
import com.example.LearningManagementSystem.Repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public ModuleEntity saveModule(ModuleEntity module) {
        return moduleRepository.save(module);
    }

    public ModuleEntity getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }

    public ModuleEntity updateModule(Long id, ModuleEntity updatedModule) {
        if (moduleRepository.existsById(id)) {
            updatedModule.setId(id);
            return moduleRepository.save(updatedModule);
        }
        return null; // Or throw an exception
    }
}

