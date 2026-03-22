package com.example.Coursera.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Coursera.DTO.Crequest;
import com.example.Coursera.Model.Course;
import com.example.Coursera.Model.Media;
import com.example.Coursera.Service.Courseservice;
import com.example.Coursera.Service.Mediaservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/courses")
public class Coursecontroller {

    @Autowired
    private Courseservice courseservice;

    @Autowired
    private Mediaservice mediaservice;
    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String Allcourses(Model model) {
    List<Course> courses = courseservice.getallcourses();

    Map<Long, Media> courseMediaMap = new HashMap<>();

    for (Course course : courses) {
        Media media = mediaservice.getFirstMediaByCourseId(course.getId());
        courseMediaMap.put(course.getId(), media);
    }

    model.addAttribute("courses", courses);
    model.addAttribute("courseMediaMap", courseMediaMap);

    return "Courselist";
}

    @GetMapping("/search/name")
    public String Coursename(@RequestParam String name, Model model) {
    List<Course> courses = courseservice.getCoursebyName(name);
    model.addAttribute("courses", courses);
    return "Courselist";
    }

    @GetMapping("/search/instructor")
    public String Instructorname(@RequestParam String instructor, Model model) {
    List<Course> courses = courseservice.getCoursebyInstructor(instructor);
    model.addAttribute("courses", courses);
    return "Courselist";
    }

   @GetMapping("/create")
   @PreAuthorize("hasRole('ADMIN')")
    public String Createform(Model model) {
    model.addAttribute("course", new Crequest());
    return "Coursecreateform";
}    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCourse(
        @ModelAttribute("course") Crequest crequest,
        @RequestParam(required = false) String fileName,
        @RequestParam(required = false) String contentType,
        @RequestParam(required = false) Long fileSize,
        @RequestParam(required = false) String fileUrl
) {
    System.out.println("fileName: " + fileName);
    System.out.println("contentType: " + contentType);
    System.out.println("fileSize: " + fileSize);
    System.out.println("fileUrl: " + fileUrl);
    
    Course savedCourse = courseservice.Createcourse(crequest);

    if (fileUrl != null && !fileUrl.isBlank()) {
        mediaservice.saveUploadedMedia(savedCourse,fileName,contentType,fileSize,fileUrl);
    }

    return "redirect:/courses/list";
}
    
    @GetMapping("/edit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editform(@PathVariable String name, Model model) {
    List<Course> courses = courseservice.getCoursebyName(name);

    if (courses.isEmpty()) {
        return "redirect:/courses/list";
    }

    model.addAttribute("course", courses.get(0));
    return "Courseeditform";
    }   

    @PostMapping("/edit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public String courseupdate(@PathVariable String name,@ModelAttribute("course") Crequest crequest) {
    courseservice.Editcourse(name, crequest);
    return "redirect:/courses/list";    
    }

    @GetMapping("/delete/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable String name) {
    courseservice.Deletecourse(name);
    return "redirect:/courses/list";
}
}
