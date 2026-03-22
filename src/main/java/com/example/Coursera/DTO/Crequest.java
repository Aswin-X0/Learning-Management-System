package com.example.Coursera.DTO;

import java.time.LocalDateTime;

import com.example.Coursera.Model.Coursestatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crequest {
     private Long id;
    private String name;
    private String instructor;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Coursestatus status;
    
}
