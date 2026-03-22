package com.example.Coursera.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Media;
import com.example.Coursera.Service.Courseservice;
import com.example.Coursera.Service.Mediaservice;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private Courseservice courseservice;
    @Autowired
    private Mediaservice mediaservice;


   @GetMapping("/courselist")
    public String Allcourses(Model model) {

    List<Course> courses = courseservice.getallcourses();

    Map<Long, Media> courseMediaMap = new HashMap<>();

    for (Course course : courses) {
        Media media = mediaservice.getFirstMediaByCourseId(course.getId());
        courseMediaMap.put(course.getId(), media);
    }

    model.addAttribute("courses", courses);
    model.addAttribute("courseMediaMap", courseMediaMap);

    return "studentcourses";
}
    @GetMapping("/courses")
    public String searchCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String instructor,
            Model model) {

        List<Course> courses;

        if (name != null && !name.trim().isEmpty()) {
            courses = courseservice.getCoursebyName(name);
        } else if (instructor != null && !instructor.trim().isEmpty()) {
            courses = courseservice.getCoursebyInstructor(instructor);
        } else {
            courses = courseservice.getallcourses();
        }
        Map<Long, Media> courseMediaMap = new HashMap<>();

         for (Course course : courses) {
        Media media = mediaservice.getFirstMediaByCourseId(course.getId());
        courseMediaMap.put(course.getId(), media);
    }

         model.addAttribute("courses", courses);
         model.addAttribute("courseMediaMap", courseMediaMap);
        return "studentcourses";
    }    
}
