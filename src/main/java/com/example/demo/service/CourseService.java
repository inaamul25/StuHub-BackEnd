package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Learning;
import com.example.demo.entity.Payment;
import com.example.demo.entity.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LearningRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LearningRepository learningRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) {
            course.setCourseName(updatedCourse.getCourseName());
            course.setPrice(updatedCourse.getPrice());
            course.setInstructor(updatedCourse.getTutor()); // Updated to getTutor
            course.setDescription(updatedCourse.getDescription());
            course.setP_link(updatedCourse.getPhoto()); // Updated to getPhoto
            course.setY_link(updatedCourse.getVideo()); // Updated to getVideo
            return courseRepository.save(course);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public String enrollUserInCourse(Long userId, Long courseId, String transactionId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment == null || !payment.getStatus().equals("completed") && !payment.getStatus().equals("paid")) {
            throw new RuntimeException("Invalid or unpaid transaction");
        }

        for (Learning learning : user.getLearningCourses()) {
            if (learning.getCourse().getId().equals(courseId)) {
                throw new RuntimeException("User already enrolled in this course");
            }
        }

        Learning learning = new Learning();
        learning.setUser(user);
        learning.setCourse(course);
        learning = learningRepository.save(learning);

        user.getLearningCourses().add(learning);
        userRepository.save(user);

        return "Successfully enrolled in course ID: " + courseId;
    }
}