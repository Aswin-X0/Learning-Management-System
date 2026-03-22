package com.example.Coursera.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Coursera.Model.Enrollments;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollments, Long>{
    List<Enrollments> findByUserId(Long userId);

    List<Enrollments> findByCourseId(Long courseId);

    Optional<Enrollments> findByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    
} 