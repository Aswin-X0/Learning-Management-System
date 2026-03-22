package com.example.Coursera.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Coursestatus;

@Repository
public interface CourseRepo extends JpaRepository <Course , Long> {
    List<Course> findByNameContainingIgnoreCase(String name);
    List<Course> findByInstructorContainingIgnoreCase(String instructor);
    List<Course> findByStatus(Coursestatus status);

} 
