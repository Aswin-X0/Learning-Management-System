package com.example.Coursera.Controllers;

import com.example.Coursera.DTO.Crequest;
import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Media;
import com.example.Coursera.Service.Courseservice;
import com.example.Coursera.Service.Mediaservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CoursecontrollerTest {

    @Mock
    private Courseservice courseservice;

    @Mock
    private Mediaservice mediaservice;

    @InjectMocks
    private Coursecontroller coursecontroller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(coursecontroller).build();
    }

    @Test
    void testAllcourses() throws Exception {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Java");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Spring");

        Media media1 = new Media();
        Media media2 = new Media();

        when(courseservice.getallcourses()).thenReturn(Arrays.asList(course1, course2));
        when(mediaservice.getFirstMediaByCourseId(1L)).thenReturn(media1);
        when(mediaservice.getFirstMediaByCourseId(2L)).thenReturn(media2);

        mockMvc.perform(get("/courses/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("Courselist"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("courseMediaMap"));

        verify(courseservice, times(1)).getallcourses();
        verify(mediaservice, times(1)).getFirstMediaByCourseId(1L);
        verify(mediaservice, times(1)).getFirstMediaByCourseId(2L);
    }

    @Test
    void testCoursename() throws Exception {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java Basics");

        List<Course> courses = Collections.singletonList(course);

        when(courseservice.getCoursebyName("Java")).thenReturn(courses);

        mockMvc.perform(get("/courses/search/name")
                        .param("name", "Java"))
                .andExpect(status().isOk())
                .andExpect(view().name("Courselist"))
                .andExpect(model().attributeExists("courses"));

        verify(courseservice, times(1)).getCoursebyName("Java");
    }

    @Test
    void testInstructorname() throws Exception {
        Course course = new Course();
        course.setId(1L);
        course.setInstructor("John");

        List<Course> courses = Collections.singletonList(course);

        when(courseservice.getCoursebyInstructor("John")).thenReturn(courses);

        mockMvc.perform(get("/courses/search/instructor")
                        .param("instructor", "John"))
                .andExpect(status().isOk())
                .andExpect(view().name("Courselist"))
                .andExpect(model().attributeExists("courses"));

        verify(courseservice, times(1)).getCoursebyInstructor("John");
    }

    @Test
    void testCreateform() throws Exception {
        mockMvc.perform(get("/courses/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("Coursecreateform"))
                .andExpect(model().attributeExists("course"));

        verifyNoInteractions(courseservice);
        verifyNoInteractions(mediaservice);
    }

    @Test
    void testCreateCourse_WithoutFileUrl() throws Exception {
        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Python");

        when(courseservice.Createcourse(any(Crequest.class))).thenReturn(savedCourse);

        mockMvc.perform(post("/courses/create")
        .param("course.name", "Python")
        .param("course.instructor", "Alice")
        .param("course.description", "Python course")
        .param("course.status", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/list"));

        verify(courseservice, times(1)).Createcourse(any(Crequest.class));
        verify(mediaservice, never()).saveUploadedMedia(any(), any(), any(), any(), any());
    }

    @Test
    void testCreateCourse_WithFileUrl() throws Exception {
        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Python");

        when(courseservice.Createcourse(any(Crequest.class))).thenReturn(savedCourse);

        mockMvc.perform(post("/courses/create")
        .param("course.name", "Python")
        .param("course.instructor", "Alice")
        .param("course.description", "Python course")
        .param("course.status", "ACTIVE")
        .param("fileName", "image.png")
        .param("contentType", "image/png")
        .param("fileSize", "12345")
        .param("fileUrl", "http://example.com/image.png"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/list"));

        verify(courseservice, times(1)).Createcourse(any(Crequest.class));
        verify(mediaservice, times(1)).saveUploadedMedia(
                eq(savedCourse),
                eq("image.png"),
                eq("image/png"),
                eq(12345L),
                eq("http://example.com/image.png")
        );
    }

    @Test
    void testEditform_Found() throws Exception {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java");

        when(courseservice.getCoursebyName("Java"))
                .thenReturn(Collections.singletonList(course));

        mockMvc.perform(get("/courses/edit/Java"))
                .andExpect(status().isOk())
                .andExpect(view().name("Courseeditform"))
                .andExpect(model().attributeExists("course"));

        verify(courseservice, times(1)).getCoursebyName("Java");
    }

    @Test
    void testEditform_NotFound() throws Exception {
        when(courseservice.getCoursebyName("Java"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courses/edit/Java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/list"));

        verify(courseservice, times(1)).getCoursebyName("Java");
    }

    @Test
    void testCourseupdate() throws Exception {
        mockMvc.perform(post("/courses/edit/Java")
                        .param("name", "Advanced Java")
                        .param("instructor", "Bob")
                        .param("description", "Updated course")
                        .param("status", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/list"));

        verify(courseservice, times(1)).Editcourse(eq("Java"), any(Crequest.class));
    }

    @Test
    void testDeleteCourse() throws Exception {
        mockMvc.perform(get("/courses/delete/Java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/list"));

        verify(courseservice, times(1)).Deletecourse("Java");
    }
}