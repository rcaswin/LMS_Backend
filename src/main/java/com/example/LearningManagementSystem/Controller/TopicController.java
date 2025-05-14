package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.InstructorEntity;
import com.example.LearningManagementSystem.Entity.ModuleEntity;
import com.example.LearningManagementSystem.Entity.TopicEntity;
import com.example.LearningManagementSystem.Repository.ModuleRepository;
import com.example.LearningManagementSystem.Repository.TopicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/videos/";

    // Create a new topic under a specific module
    @PostMapping(value = "/module/{moduleId}/topics/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleTopicsWithFiles(
            @PathVariable Long moduleId,
            @RequestPart("topicDetails") String topicDetailsJson,
            @RequestPart("files") MultipartFile[] files) throws IOException {

        Optional<ModuleEntity> moduleOpt = moduleRepository.findById(moduleId);
        if (moduleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        // Convert JSON to List<TopicEntity>
        List<TopicEntity> topics = Arrays.asList(objectMapper.readValue(
                topicDetailsJson, TopicEntity[].class));

        if (topics.size() != files.length) {
            return ResponseEntity.badRequest().body("Number of topics and files must match");
        }

        // Save files and assign to topic
        for (int i = 0; i < topics.size(); i++) {
            MultipartFile file = files[i];
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + filename);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            topics.get(i).setFile(filename);
        }

        // Add to module
        ModuleEntity module = moduleOpt.get();
        module.getTopics().addAll(topics);
        moduleRepository.save(module); // cascade saves topics too

        return ResponseEntity.status(HttpStatus.CREATED).body(topics);
    }

    // Get all topics
    @GetMapping
    public ResponseEntity<List<TopicEntity>> getAllTopics() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    // Get topic by ID
    @GetMapping("/{id}")
    public ResponseEntity<TopicEntity> getTopicById(@PathVariable Long id) {
        Optional<TopicEntity> topic = topicRepository.findById(id);
        return topic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a topic
//    @PutMapping("/{id}")
//    public ResponseEntity<TopicEntity> updateTopic(
//            @PathVariable Long id,
//            @RequestBody TopicEntity updatedTopic) {
//
//        Optional<TopicEntity> existingTopicOpt = topicRepository.findById(id);
//        if (!existingTopicOpt.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        TopicEntity existingTopic = existingTopicOpt.get();
//        existingTopic.setTitle(updatedTopic.getTitle());
//        existingTopic.setFile(updatedTopic.getFile());
//        existingTopic.setPreview(updatedTopic.isPreview());
//        existingTopic.setWatch(updatedTopic.isWatch());
//        // Add MCQ quiz fields if added in your entity
//
//        return ResponseEntity.ok(topicRepository.save(existingTopic));
//    }

    // Delete a topic
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        if (!topicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

