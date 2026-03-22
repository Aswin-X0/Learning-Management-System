package com.example.Coursera.Service;

import com.example.Coursera.DTO.Crequest;
import com.example.Coursera.Model.Course;
import com.example.Coursera.Repositories.CourseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseserviceTest {

    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private Courseservice courseservice;

    @Test
    void testGetallcourses() {
        Course course1 = new Course();
        course1.setName("Java");
        course1.setInstructor("Alice");

        Course course2 = new Course();
        course2.setName("Spring Boot");
        course2.setInstructor("Bob");

        when(courseRepo.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseservice.getallcourses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Spring Boot", result.get(1).getName());

        verify(courseRepo, times(1)).findAll();
    }

    @Test
    void testGetCoursebyName() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setInstructor("Alice");

        when(courseRepo.findByNameContainingIgnoreCase("java"))
                .thenReturn(Collections.singletonList(course));

        List<Course> result = courseservice.getCoursebyName("java");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Basics", result.get(0).getName());

        verify(courseRepo, times(1)).findByNameContainingIgnoreCase("java");
    }

    @Test
    void testGetCoursebyInstructor() {
        Course course = new Course();
        course.setName("Spring Boot");
        course.setInstructor("Bob");

        when(courseRepo.findByInstructorContainingIgnoreCase("bob"))
                .thenReturn(Collections.singletonList(course));

        List<Course> result = courseservice.getCoursebyInstructor("bob");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getInstructor());

        verify(courseRepo, times(1)).findByInstructorContainingIgnoreCase("bob");
    }

    @Test
    void testCreatecourse() {
        Crequest crequest = new Crequest();
        crequest.setName("Python");
        crequest.setInstructor("David");
        crequest.setDescription("Python course");


        Course savedCourse = new Course();
        savedCourse.setName("Python");
        savedCourse.setInstructor("David");
        savedCourse.setDescription("Python course");


        when(courseRepo.save(any(Course.class))).thenReturn(savedCourse);

        Course result = courseservice.Createcourse(crequest);

        assertNotNull(result);
        assertEquals("Python", result.getName());
        assertEquals("David", result.getInstructor());
        assertEquals("Python course", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(courseRepo, times(1)).save(any(Course.class));
    }

    @Test
    void testEditcourse_Found() {
        Course existingCourse = new Course();
        existingCourse.setName("Java");
        existingCourse.setInstructor("Old Instructor");
        existingCourse.setDescription("Old description");

        Crequest crequest = new Crequest();
        crequest.setName("Advanced Java");
        crequest.setInstructor("New Instructor");
        crequest.setDescription("Updated description");

        Course updatedCourse = new Course();
        updatedCourse.setName("Advanced Java");
        updatedCourse.setInstructor("New Instructor");
        updatedCourse.setDescription("Updated description");


        when(courseRepo.findByNameContainingIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(existingCourse));
        when(courseRepo.save(existingCourse)).thenReturn(updatedCourse);

        Course result = courseservice.Editcourse("Java", crequest);

        assertNotNull(result);
        assertEquals("Advanced Java", result.getName());
        assertEquals("New Instructor", result.getInstructor());
        assertEquals("Updated description", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());

        verify(courseRepo, times(1)).findByNameContainingIgnoreCase("Java");
        verify(courseRepo, times(1)).save(existingCourse);
    }

    @Test
    void testEditcourse_NotFound() {
        Crequest crequest = new Crequest();
        crequest.setName("Advanced Java");
        crequest.setInstructor("New Instructor");
        crequest.setDescription("Updated description");

        when(courseRepo.findByNameContainingIgnoreCase("Java"))
                .thenReturn(Collections.emptyList());

        Course result = courseservice.Editcourse("Java", crequest);

        assertNull(result);

        verify(courseRepo, times(1)).findByNameContainingIgnoreCase("Java");
        verify(courseRepo, never()).save(any(Course.class));
    }

    @Test
    void testDeletecourse_Found() {
        Course course = new Course();
        course.setName("Java");

        when(courseRepo.findByNameContainingIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(course));

        courseservice.Deletecourse("Java");

        verify(courseRepo, times(1)).findByNameContainingIgnoreCase("Java");
        verify(courseRepo, times(1)).delete(course);
    }

    @Test
    void testDeletecourse_NotFound() {
        when(courseRepo.findByNameContainingIgnoreCase("Java"))
                .thenReturn(Collections.emptyList());

        courseservice.Deletecourse("Java");

        verify(courseRepo, times(1)).findByNameContainingIgnoreCase("Java");
        verify(courseRepo, never()).delete(any(Course.class));
    }
}