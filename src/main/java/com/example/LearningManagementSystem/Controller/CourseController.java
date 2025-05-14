package com.example.LearningManagementSystem.Controller;

import com.example.LearningManagementSystem.Entity.*;
import com.example.LearningManagementSystem.Service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*") // Enable CORS if frontend is separate
public class CourseController {

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/course_images/";
    private final String SRC_UPLOAD_DIR = System.getProperty("user.dir") + "/course_resources/";
    private final String VDO_UPLOAD_DIR = System.getProperty("user.dir") + "/course_resources/";

    @Autowired
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCourse(
            @RequestPart("courseDetails") String courseDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "resourceFiles") List<MultipartFile> resourceFiles,
            @RequestPart(value = "videoFiles") List<MultipartFile> videoFiles
    ) {
        try {
            CourseEntity courseEntity = objectMapper.readValue(courseDetails, CourseEntity.class);

            // Create all necessary directories
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            Files.createDirectories(Paths.get(SRC_UPLOAD_DIR));
            Files.createDirectories(Paths.get(VDO_UPLOAD_DIR));

            // Save course image
            String imageFilename = sanitizeFilename(image.getOriginalFilename());
            Path imagePath = Paths.get(UPLOAD_DIR + imageFilename);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            courseEntity.setImage(imageFilename);

            // Save resource files (PDFs, etc.)
            if (resourceFiles != null && !resourceFiles.isEmpty()) {
                List<CourseResourceEntity> resources = new ArrayList<>();
                for (MultipartFile file : resourceFiles) {
                    String originalName = sanitizeFilename(file.getOriginalFilename());
                    String fileName = UUID.randomUUID() + "_" + originalName;
                    Path path = Paths.get(SRC_UPLOAD_DIR + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    String extension = getFileExtension(file);
                    CourseResourceEntity resource = new CourseResourceEntity();
                    resource.setTitle(file.getOriginalFilename());
                    resource.setFile(fileName);
                    resource.setType(extension);
                    resources.add(resource);
                }
                courseEntity.setCourseResources(resources);
            }

            // Save topic videos
            if (videoFiles != null && !videoFiles.isEmpty()) {
                int videoIndex = 0;
                for (ModuleEntity module : courseEntity.getModules()) {
                    for (TopicEntity topic : module.getTopics()) {
                        if (videoIndex < videoFiles.size()) {
                            MultipartFile videoFile = videoFiles.get(videoIndex++);
                            String videoName = UUID.randomUUID() + "_" + sanitizeFilename(videoFile.getOriginalFilename());
                            Path videoPath = Paths.get(VDO_UPLOAD_DIR + videoName);
                            Files.copy(videoFile.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
                            topic.setFile(videoName);
                        }
                    }
                }
            }

            // Save course entity
            CourseEntity saved = courseService.saveCourse(courseEntity);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            e.printStackTrace(); // Optional: print to logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading files: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Optional: print to logs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    private String getFileExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9\\.\\-\\_]", "_");
    }

    @GetMapping("/all")
    public List<CourseEntity> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseEntity> getCourseById(@PathVariable String id) {
        CourseEntity course = courseService.getCourseById(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseEntity> updateCourse(@PathVariable String id, @RequestBody CourseEntity updatedCourse) {
        CourseEntity updated = courseService.updateCourse(id, updatedCourse);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    @GetMapping("/instructor/{instructorId}")
    public List<CourseEntity> getCoursesByInstructorId(@PathVariable String instructorId) {
        return courseService.getCoursesByInstructorId(instructorId);
    }
}
