package com.example.Coursera.Model;
import java.time.LocalDateTime;

    import jakarta.persistence.Id;

    import jakarta.persistence.Entity;
    import jakarta.persistence.EnumType;
    import jakarta.persistence.Enumerated;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table( name= "Courses")

    public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String instructor;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        private Coursestatus status;
        
    }
