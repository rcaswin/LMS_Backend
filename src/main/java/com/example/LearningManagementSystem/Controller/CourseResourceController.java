package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.CourseResourceEntity;
import com.example.LearningManagementSystem.Service.CourseResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class CourseResourceController {

    private static final String UPLOAD_DIR = "uploads/resources/";

    @Autowired
    private CourseResourceService resourceService;

    @Autowired
    private ObjectMapper objectMapper;

    // Upload multiple resources
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CourseResourceEntity>> uploadMultipleResources(
            @RequestPart("resources") String resourcesJson,
            @RequestPart("files") MultipartFile[] files) throws IOException {

        List<CourseResourceEntity> resources = Arrays.asList(
                objectMapper.readValue(resourcesJson, CourseResourceEntity[].class));

        if (resources.size() != files.length) {
            return ResponseEntity.badRequest().build();
        }

        List<CourseResourceEntity> savedResources = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + filename);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            CourseResourceEntity res = resources.get(i);
            res.setFile(filename);
            savedResources.add(res);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.saveAll(savedResources));
    }

    @GetMapping
    public ResponseEntity<List<CourseResourceEntity>> getAll() {
        return ResponseEntity.ok(resourceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResourceEntity> getById(@PathVariable Long id) {
        CourseResourceEntity resource = resourceService.getById(id);
        if (resource == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
