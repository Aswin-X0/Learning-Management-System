package com.example.Coursera.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Enrollments;
import com.example.Coursera.Model.User;
import com.example.Coursera.Repositories.CourseRepo;
import com.example.Coursera.Repositories.EnrollmentRepo;
import com.example.Coursera.Repositories.UserRepo;


@Service
public class Enrollmentservice {

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseRepo courseRepo;


    public List<Enrollments> getAllEnrollments() {
        return enrollmentRepo.findAll();
    }

    public List<Enrollments> userenrollment(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return enrollmentRepo.findByUserId(userId);
    }

    public Enrollments enrollUser(Long userId, Long courseId) {
    User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    if (enrollmentRepo.existsByUserIdAndCourseId(userId, courseId)) {
        throw new RuntimeException("User already enrolled in this course");
    }

    Enrollments enrollment = new Enrollments();
    enrollment.setUser(user);
    enrollment.setCourse(course);

    return enrollmentRepo.save(enrollment);
}

     public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepo.existsById(enrollmentId)) {
            throw new RuntimeException("Enrollment not found");
        }
        enrollmentRepo.deleteById(enrollmentId);
    }
    
}
