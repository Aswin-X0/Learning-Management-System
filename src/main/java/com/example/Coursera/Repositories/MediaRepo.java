package com.example.Coursera.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Coursera.Model.Media;

@Repository
public interface MediaRepo extends JpaRepository<Media , Long>{
    List<Media> findByCourseId(Long courseId);
    Optional<Media> findFirstByCourseId(Long courseId);
  
} 