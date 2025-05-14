package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.ModuleEntity;
import com.example.LearningManagementSystem.Service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ModuleEntity> createModule(@RequestBody ModuleEntity module) {
        ModuleEntity savedModule = moduleService.saveModule(module);
        return ResponseEntity.ok(savedModule);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleEntity> getModuleById(@PathVariable Long id) {
        ModuleEntity module = moduleService.getModuleById(id);
        return module != null ? ResponseEntity.ok(module) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleEntity> updateModule(@PathVariable Long id, @RequestBody ModuleEntity updatedModule) {
        ModuleEntity updated = moduleService.updateModule(id, updatedModule);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}
