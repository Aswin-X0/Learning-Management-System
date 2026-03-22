package com.example.Coursera.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Service.Userservice;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/Coursera")
public class Usercontroller {

    @Autowired
    private Userservice userservice;

    @GetMapping("/home")
    public String Homepage(HttpSession session, Model model) {
    User user = (User) session.getAttribute("loggedInUser");  
    model.addAttribute("user", user); 
    return "Homepage";
    }
    
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Admindashboard";
    }

    @GetMapping("/userlist")
    @PreAuthorize("hasRole('ADMIN')")
    public String Userlist(Model model) {
        model.addAttribute("users", userservice.getAllUsers());
        return "Userlist";
    }
    @GetMapping("/useredit/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editform(@PathVariable String username, Model model) {
        User user = userservice.getUserbyUsername(username);
    if (user == null) {
        return "redirect:/vibes/home";
    }
    model.addAttribute("user", user);
    return "usereditform";
}
    @PostMapping("/useredit/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userupdate(@PathVariable String username,@ModelAttribute("user") Urequest urequest) {
        userservice.Updateuser(username,urequest );
        return "redirect:/Coursera/userlist";
}
    @GetMapping("/deleteuser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userdeletion(@PathVariable Long id) {
        userservice.Deleteuser(id);
        return "redirect:/Coursera/userlist";
   }    
}
