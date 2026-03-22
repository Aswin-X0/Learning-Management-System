package com.example.Coursera.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Coursera.Config.JwtUtil;
import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Repositories.UserRepo;

@Service
public class Authservice {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    public String SignUp(Urequest urequest) {
         if (userRepo.findByUsername(urequest.getUsername()) != null) {
        return "Username already exists";
    }
        User user = new User();
        user.setName(urequest.getName());
        user.setAge(urequest.getAge());
        user.setNumber(urequest.getNumber());
        user.setEmail(urequest.getEmail());
        user.setUsername(urequest.getUsername());
        user.setPassword(passwordEncoder.encode(urequest.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }


    public String SignIn(String username , String password){ 
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(token); 
        return jwtUtil.generateToken(auth.getName()); 
    }

    public Authentication authenticateUser(String username, String password) {
    UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(username, password);
    return authManager.authenticate(token);
}
}
