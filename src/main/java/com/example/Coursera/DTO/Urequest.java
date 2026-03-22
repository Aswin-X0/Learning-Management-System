package com.example.Coursera.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Urequest {
    private String name;
    private Integer age;
    private String number;
    private String email;
    private String username;
    private String password;
    private String role;
}
