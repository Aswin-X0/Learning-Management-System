package com.example.Coursera.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Coursera.Service.Userservice;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private  Userservice userService;

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String token = null;

    // 1. Read from cookie
    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
    }

    String authHeader = request.getHeader("Authorization");
    if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
    }

    if (token != null && jwtUtil.validateToken(token)) {

        String username = jwtUtil.getUsernameFromToken(token);

        UserDetails userDetails = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        // ✅ YOUR actual entity (for session)
        com.example.Coursera.Model.User user = userService.getUserbyUsername(username);

        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", user);
    }

    filterChain.doFilter(request, response);
}
}

