package com.example.Coursera.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.Coursera.Model.Enrollments;
import com.example.Coursera.Model.User;
import com.example.Coursera.Service.Enrollmentservice;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/enroll")
public class EnrollmentController {
    
    @Autowired
    private Enrollmentservice enrollmentservice;

    @GetMapping("/enrollmentlist")
    @PreAuthorize("hasRole('ADMIN')")
    public String enrollments(Model model) {
        model.addAttribute("enrollments", enrollmentservice.getAllEnrollments());
        return "enrollments";
    }
    @GetMapping("/user/{userId}")
    public String getUserEnrollments(@PathVariable Long userId, Model model) {
        List<Enrollments> enrollments = enrollmentservice.userenrollment(userId);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("userId", userId);
        return "userenrollments";
    }
    @PostMapping("/course")
    public String enrollCourse(@RequestParam Long courseId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/Coursera/login";
        }

        try {
        enrollmentservice.enrollUser(loggedInUser.getId(), courseId);
        redirectAttributes.addFlashAttribute("success", "Enrolled successfully!");
        }catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

        return "redirect:/search/courselist";
    }    


    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id, @RequestParam Long userId) {
        enrollmentservice.deleteEnrollment(id);
        return "redirect:/enroll/user/" + userId;
    }
}
