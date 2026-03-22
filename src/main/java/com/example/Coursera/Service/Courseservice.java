package com.example.Coursera.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.Coursera.DTO.Crequest;
import com.example.Coursera.Model.Course;
import com.example.Coursera.Repositories.CourseRepo;

@Service
public class Courseservice {

    @Autowired
    private CourseRepo courseRepo;

    public List<Course>getallcourses(){
        return courseRepo.findAll();      
    }

    public List<Course> getCoursebyName(String name){
        return courseRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Course> getCoursebyInstructor(String instructor){
        return courseRepo.findByInstructorContainingIgnoreCase(instructor);
    }

    public Course Createcourse(Crequest crequest){
        Course course = new Course();
        course.setName(crequest.getName());
        course.setInstructor(crequest.getInstructor());
        course.setDescription(crequest.getDescription());
        course.setStatus(crequest.getStatus());
        return courseRepo.save(course);

    }
    public Course Editcourse(String name, Crequest crequest){
        Course existingCourse = courseRepo.findByNameContainingIgnoreCase(name).stream().findFirst().orElse(null); 
            if(existingCourse != null){
                existingCourse.setName(crequest.getName());
                existingCourse.setInstructor(crequest.getInstructor());
                existingCourse.setDescription(crequest.getDescription());
                existingCourse.setStatus(crequest.getStatus());
                return courseRepo.save(existingCourse);
            }     
            return null;
    }

    public void Deletecourse(String name){
        courseRepo.findByNameContainingIgnoreCase(name).stream().findFirst().ifPresent(course -> courseRepo.delete(course));
    }
}


