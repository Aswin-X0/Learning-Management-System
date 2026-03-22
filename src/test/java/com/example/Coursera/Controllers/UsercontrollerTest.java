package com.example.Coursera.Controllers;

import com.example.Coursera.DTO.Urequest;
import com.example.Coursera.Model.User;
import com.example.Coursera.Service.Userservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UsercontrollerTest {

    @Mock
    private Userservice userservice;

    @InjectMocks
    private Usercontroller usercontroller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usercontroller).build();
    }

    @Test
    void testHomepage_WithLoggedInUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass", "USER");
        session.setAttribute("loggedInUser", user);

        mockMvc.perform(get("/Coursera/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("Homepage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testHomepage_WithoutLoggedInUser() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/Coursera/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("Homepage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", (Object) null));
    }

    @Test
    void testAdminDashboard() throws Exception {
        mockMvc.perform(get("/Coursera/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("Admindashboard"));
    }

    @Test
    void testUserlist() throws Exception {
        User user1 = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");
        User user2 = new User(2L, "Alice", 25, "9999999999", "alice@example.com", "alice", "pass2", "ADMIN");

        when(userservice.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/Coursera/userlist"))
                .andExpect(status().isOk())
                .andExpect(view().name("Userlist"))
                .andExpect(model().attributeExists("users"));

        verify(userservice, times(1)).getAllUsers();
    }

    @Test
    void testEditform_UserFound() throws Exception {
        User user = new User(1L, "John", 22, "9876543210", "john@example.com", "john", "pass1", "USER");

        when(userservice.getUserbyUsername("john")).thenReturn(user);

        mockMvc.perform(get("/Coursera/useredit/john"))
                .andExpect(status().isOk())
                .andExpect(view().name("usereditform"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));

        verify(userservice, times(1)).getUserbyUsername("john");
    }

    @Test
    void testEditform_UserNotFound() throws Exception {
        when(userservice.getUserbyUsername("john")).thenReturn(null);

        mockMvc.perform(get("/Coursera/useredit/john"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vibes/home"));

        verify(userservice, times(1)).getUserbyUsername("john");
    }

    @Test
    void testUserupdate() throws Exception {
        mockMvc.perform(post("/Coursera/useredit/john")
                        .param("user.name", "John Updated")
                        .param("user.age", "24")
                        .param("user.number", "9999999999")
                        .param("user.email", "johnupdated@example.com")
                        .param("user.username", "john")
                        .param("user.password", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/Coursera/userlist"));

        verify(userservice, times(1)).Updateuser(eq("john"), any(Urequest.class));
    }

    @Test
    void testUserdeletion() throws Exception {
        mockMvc.perform(get("/Coursera/deleteuser/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/Coursera/userlist"));

        verify(userservice, times(1)).Deleteuser(1L);
    }
}