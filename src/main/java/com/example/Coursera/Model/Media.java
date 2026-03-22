package com.example.Coursera.Model;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MediaFiles")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String mediaType;   // VIDEO, PDF, IMAGE, AUDIO
    private String fileUrl;
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
