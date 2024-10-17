package com.habittracker.service;
/*
import com.habittracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testRegisterUser() {

        String result = userService.registerUser("John Doe", "john@example.com", "password");
        assertEquals("The registration was successful.", result);

        result = userService.registerUser("Jane Doe", "john@example.com", "password");
        assertEquals("Error: A user with this email address is already registered.", result);
    }

    @Test
    void testLoginUser() {
        userService.registerUser("John Doe", "john@example.com", "password");

        String result = userService.loginUser("john@example.com", "password");
        assertEquals("Authorization was successful. Welcome, John Doe!", result);

        result = userService.loginUser("jane@example.com", "password");
        assertEquals("Error: No user with this email was found.", result);

        result = userService.loginUser("john@example.com", "wrongpassword");
        assertEquals("Error: Incorrect password.", result);
    }

    @Test
    void testUpdateUserProfile() {
        userService.registerUser("John Doe", "john@example.com", "password");

        String result = userService.updateUserProfile("john@example.com", "John Smith", "john.smith@example.com");
        assertEquals("The profile has been successfully updated.", result);

        userService.registerUser("Jane Doe", "jane@example.com", "password");
        result = userService.updateUserProfile("john.smith@example.com", "John Smith", "jane@example.com");
        assertEquals("Error: The new email is already taken by another user.", result);

        result = userService.updateUserProfile("nonexistent@example.com", "John Smith", "john.smith@example.com");
        assertEquals("Error: User not found.", result);
    }

    @Test
    void testDeleteUser() {
        userService.registerUser("John Doe", "john@example.com", "password");

        String result = userService.deleteUser("john@example.com");
        assertEquals("The user account has been successfully deleted.", result);

        result = userService.deleteUser("john@example.com");
        assertEquals("Error: User not found.", result);
    }

    @Test
    void testResetPassword() {
        userService.registerUser("John Doe", "john@example.com", "password");

        String result = userService.resetPassword("john@example.com");
        assertEquals("Temporary password sent to john@example.com: temp1234", result);

        result = userService.resetPassword("nonexistent@example.com");
        assertEquals("Error: User not found.", result);
    }

    @Test
    void testGetUserByEmail() {
        userService.registerUser("John Doe", "john@example.com", "password");

        User user = userService.getUserByEmail("john@example.com");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());

        user = userService.getUserByEmail("nonexistent@example.com");
        assertNull(user);
    }

    @Test
    void testGetAllUsers() {
        userService.registerUser("John Doe", "john@example.com", "password");
        userService.registerUser("Jane Doe", "jane@example.com", "password");

        Map<String, User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertFalse(users.containsKey("admin"));
    }
}*/