package com.example.Coursera.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        User user = new User();

        user.setId(1L);
        user.setName("John");
        user.setAge(22);
        user.setNumber("9876543210");
        user.setEmail("john@example.com");
        user.setUsername("john123");
        user.setPassword("secret");
        user.setRole("STUDENT");

        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals(22, user.getAge());
        assertEquals("9876543210", user.getNumber());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("john123", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("STUDENT", user.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(
                1L,
                "Alice",
                25,
                "9999999999",
                "alice@example.com",
                "alice123",
                "mypassword",
                "ADMIN"
        );

        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals(25, user.getAge());
        assertEquals("9999999999", user.getNumber());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("alice123", user.getUsername());
        assertEquals("mypassword", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User(
                1L,
                "Bob",
                30,
                "8888888888",
                "bob@example.com",
                "bob123",
                "pass123",
                "USER"
        );

        User user2 = new User(
                1L,
                "Bob",
                30,
                "8888888888",
                "bob@example.com",
                "bob123",
                "pass123",
                "USER"
        );

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToStringDoesNotContainPassword() {
        User user = new User(
                1L,
                "David",
                28,
                "7777777777",
                "david@example.com",
                "david123",
                "hiddenPassword",
                "USER"
        );

        String result = user.toString();

        assertNotNull(result);
        assertFalse(result.contains("hiddenPassword"));
        assertFalse(result.contains("password"));
        assertTrue(result.contains("David"));
        assertTrue(result.contains("david@example.com"));
    }

    @Test
    void testEntityAnnotationPresent() {
        assertTrue(User.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void testTableAnnotationValue() {
        Table table = User.class.getAnnotation(Table.class);

        assertNotNull(table);
        assertEquals("USERS", table.name());
    }

    @Test
    void testIdFieldAnnotations() throws NoSuchFieldException {
        Field idField = User.class.getDeclaredField("id");

        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(GeneratedValue.class));

        GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
        assertEquals(GenerationType.IDENTITY, generatedValue.strategy());
    }
}