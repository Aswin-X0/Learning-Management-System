package com.example.Coursera.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Service.Authservice;
import com.example.Coursera.Service.Userservice;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Coursera")
public class AuthController {

   @Autowired
   private Authservice authService;
   @Autowired
   private Userservice userservice;

   @ModelAttribute("urequest")
    public Urequest urequest() {
        return new Urequest();
    }

   @GetMapping("/signup")
    @PreAuthorize("permitAll()")
    public String Signup(){
        return "Register";
    }

   @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    public String signUp(@ModelAttribute("urequest") Urequest urequest, Model model) {
    String result = authService.SignUp(urequest);

        if (!result.equals("User registered successfully")) {
            model.addAttribute("error", result);
            return "Register";
        }

        return "redirect:/Coursera/login";
    }

   @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String loginPage() {
        return "Login";
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public String signIn(@RequestParam String username,
                     @RequestParam String password,
                     HttpServletResponse response,
                     HttpSession session,
                     Model model) {

    try {
        String jwt = authService.SignIn(username, password);

        User user = userservice.getUserbyUsername(username);

        session.setAttribute("user", user);


        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return "redirect:/Coursera/home";

    } catch (Exception e) {
        model.addAttribute("error", "Invalid username or password");
        return "Login";
    }
}
}

