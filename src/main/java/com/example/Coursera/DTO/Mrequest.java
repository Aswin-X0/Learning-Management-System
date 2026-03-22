package com.example.Coursera.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mrequest {
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String fileUrl;
    
}
