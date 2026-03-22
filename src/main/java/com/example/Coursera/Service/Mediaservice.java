package com.example.Coursera.Service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Media;
import com.example.Coursera.Repositories.MediaRepo;

@Service
public class Mediaservice {

    @Autowired
    private MediaRepo mediaRepo;

    public Media saveUploadedMedia(
            Course course,
            String fileName,
            String contentType,
            Long fileSize,
            String fileUrl
    ) {
        validateUploadcareData(fileName, contentType, fileSize, fileUrl);

        Media media = new Media();
        media.setCourse(course);
        media.setFileName(fileName);
        media.setContentType(contentType);
        media.setFileSize(fileSize);
        media.setMediaType(determineMediaType(contentType));
        media.setFileUrl(fileUrl);
        media.setUploadedAt(LocalDateTime.now());

        return mediaRepo.save(media);
    }
    
    public Media getFirstMediaByCourseId(Long courseId) {
    return mediaRepo.findFirstByCourseId(courseId).orElse(null);
    }
    

    private void validateUploadcareData(
            String fileName,
            String contentType,
            Long fileSize,
            String fileUrl
    ) {
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new RuntimeException("File URL is required");
        }

        if (fileName == null || fileName.isBlank()) {
            fileName = "uploaded-file";
        }

        if (contentType == null || contentType.isBlank()) {
            throw new RuntimeException("Content type is required");
        }

        if (fileSize == null || fileSize <= 0) {
            throw new RuntimeException("Invalid file size");
        }

        boolean allowed = contentType.startsWith("video/")
                || contentType.equals("application/pdf")
                || contentType.startsWith("image/")
                || contentType.startsWith("audio/");

        if (!allowed) {
            throw new RuntimeException("Only video, PDF, image, and audio files are allowed");
        }
    }

    private String determineMediaType(String contentType) {
        if (contentType == null) return "UNKNOWN";
        if (contentType.startsWith("video/")) return "VIDEO";
        if (contentType.equals("application/pdf")) return "PDF";
        if (contentType.startsWith("image/")) return "IMAGE";
        if (contentType.startsWith("audio/")) return "AUDIO";
        return "UNKNOWN";
    }
    
    
}
