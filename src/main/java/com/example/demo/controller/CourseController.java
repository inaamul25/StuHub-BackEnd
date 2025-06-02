package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    // ✅ Updated POST method to accept form data + image file
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCourse(
            @RequestParam("courseName") String courseName,
            @RequestParam("tutor") String tutor,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("video") String video,
            @RequestParam("photo") MultipartFile photo
    ) {
        try {
            // Save image to uploads/ folder
            String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path uploadPath = Paths.get("uploads", fileName);
            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, photo.getBytes());

            // Build full URL (optional: you can use just "/uploads/...") 
            String imageUrl = "/uploads/" + fileName;

            Course course = new Course();
            course.setCourseName(courseName);
            course.setTutor(tutor);
            course.setPrice((int) price); // If your entity has int
            course.setDescription(description);
            course.setVideo(video);
            course.setPhoto(imageUrl);

            Course savedCourse = courseService.createCourse(course);
            return ResponseEntity.ok(savedCourse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add course");
        }
    }

    @PostMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return courseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
