package com.example.demo.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import com.example.demo.service.PaymentService;
import com.example.demo.dto.EnrollRequest;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return courseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/{id}/enroll")
    public String enrollInCourse(@PathVariable Long id, @RequestBody EnrollRequest enrollRequest) throws RazorpayException {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return "Course not found";
        }

        // Convert int to Double for payment service
        String transactionId = paymentService.createPaymentOrder(
            enrollRequest.getUserId(),
            id,
            Double.valueOf(course.getPrice())
        );

        // Return transaction ID to frontend for payment processing
        return transactionId;
    }
}































































