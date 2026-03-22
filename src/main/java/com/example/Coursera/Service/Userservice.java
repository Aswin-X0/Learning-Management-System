package com.example.Coursera.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Repositories.UserRepo;


@Service
public class Userservice implements UserDetailsService{
    
    @Autowired
    private UserRepo userRepo;


    public List<User>getAllUsers(){
        return userRepo.findAll();
    }

    public User getUserbyId(Long id){
    return userRepo.findById(id).orElse(null);
    }

    public User getUserbyUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User Createuser(Urequest urequest){
        User user = new User();
        user.setName(urequest.getName());
        user.setAge(urequest.getAge());
        user.setNumber(urequest.getNumber());
        user.setEmail(urequest.getEmail());
        user.setUsername(urequest.getUsername());
        user.setPassword(urequest.getPassword());    
        return userRepo.save(user);
    }

    public User Updateuser(String username, Urequest urequest){
        User existingsUser = userRepo.findByUsername(username);
            if(existingsUser != null){
                existingsUser.setName(urequest.getName());
                existingsUser.setAge(urequest.getAge());
                existingsUser.setEmail(urequest.getEmail());
                existingsUser.setNumber(urequest.getNumber());
                return userRepo.save(existingsUser);
            }     
            return null;
    }

    public void Deleteuser(Long id){
        userRepo.deleteById(id);
    }

    public User validateLogin(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user == null) return null;
        if (user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepo.findByUsername(username);
                if (appUser == null) {
                throw new UsernameNotFoundException("User not found");
    }
        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole() != null ? appUser.getRole() : "USER")
                .build();
    }


}    
