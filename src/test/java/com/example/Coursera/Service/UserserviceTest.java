package com.example.Coursera.Service;

import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserserviceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private Userservice userservice;

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");
        User user2 = new User(2L, "Alice", 25, "9999999999", "alice@example.com", "alice", "pass2", "ADMIN");

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userservice.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("Alice", users.get(1).getName());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void testGetUserbyId_Found() {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        User result = userservice.getUserbyId(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void testGetUserbyId_NotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        User result = userservice.getUserbyId(1L);

        assertNull(result);
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void testGetUserbyUsername() {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userservice.getUserbyUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testCreateuser() {
        Urequest urequest = new Urequest();
        urequest.setName("John");
        urequest.setAge(22);
        urequest.setNumber("9876543210");
        urequest.setEmail("john@example.com");
        urequest.setUsername("john");
        urequest.setPassword("pass1");

        User savedUser = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", null);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userservice.Createuser(urequest);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john", result.getUsername());
        assertEquals("pass1", result.getPassword());

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateuser_Found() {
        User existingUser = new User(1L, "Old Name", 20, "1111111111", "old@example.com", "john", "pass1", "USER");

        Urequest urequest = new Urequest();
        urequest.setName("New Name");
        urequest.setAge(25);
        urequest.setEmail("new@example.com");
        urequest.setNumber("9999999999");

        User updatedUser = new User(1L, "New Name", 25, "9999999999", "new@example.com", "john", "pass1", "USER");

        when(userRepo.findByUsername("john")).thenReturn(existingUser);
        when(userRepo.save(existingUser)).thenReturn(updatedUser);

        User result = userservice.Updateuser("john", urequest);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(25, result.getAge());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("9999999999", result.getNumber());

        verify(userRepo, times(1)).findByUsername("john");
        verify(userRepo, times(1)).save(existingUser);
    }

    @Test
    void testUpdateuser_NotFound() {
        Urequest urequest = new Urequest();
        urequest.setName("New Name");

        when(userRepo.findByUsername("john")).thenReturn(null);

        User result = userservice.Updateuser("john", urequest);

        assertNull(result);
        verify(userRepo, times(1)).findByUsername("john");
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testDeleteuser() {
        doNothing().when(userRepo).deleteById(1L);

        userservice.Deleteuser(1L);

        verify(userRepo, times(1)).deleteById(1L);
    }

    @Test
    void testValidateLogin_Success() {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userservice.validateLogin("john", "pass1");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testValidateLogin_UserNotFound() {
        when(userRepo.findByUsername("john")).thenReturn(null);

        User result = userservice.validateLogin("john", "pass1");

        assertNull(result);
        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testValidateLogin_WrongPassword() {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userservice.validateLogin("john", "wrongpass");

        assertNull(result);
        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testValidateLogin_NullPasswordInDatabase() {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", null, "USER");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userservice.validateLogin("john", "pass1");

        assertNull(result);
        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testLoadUserByUsername_Success_WithRole() {
        User appUser = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "ADMIN");

        when(userRepo.findByUsername("john")).thenReturn(appUser);

        UserDetails userDetails = userservice.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("pass1", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testLoadUserByUsername_Success_DefaultRole() {
        User appUser = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", null);

        when(userRepo.findByUsername("john")).thenReturn(appUser);

        UserDetails userDetails = userservice.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("pass1", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(userRepo, times(1)).findByUsername("john");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepo.findByUsername("john")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userservice.loadUserByUsername("john"));

        verify(userRepo, times(1)).findByUsername("john");
    }
}