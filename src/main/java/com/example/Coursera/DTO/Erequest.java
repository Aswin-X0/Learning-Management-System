package com.example.Coursera.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Erequest {
    private Long userId;
    private Long courseId;
    private String courseName;
    private LocalDateTime enrolled;
}
