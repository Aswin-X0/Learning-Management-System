package com.example.Coursera.Model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Enrollments" ,  uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "course_id"})})
public class Enrollments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime enrolled;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Formula("(select c.name from courses c where c.id = course_id)")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @PrePersist
    public void setEnrollTime() {
        if (this.enrolled == null) {
            this.enrolled = LocalDateTime.now();
        }
    }
}
