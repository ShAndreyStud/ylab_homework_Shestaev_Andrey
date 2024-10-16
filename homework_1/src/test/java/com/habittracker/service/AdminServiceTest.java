package com.habittracker.service;

import com.habittracker.model.User;
import com.habittracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testBlockUser_NoActiveUsers() {
        when(userService.getAllUsers()).thenReturn(new HashMap<>());

        adminService.blockUser(scanner);

        verify(userService, times(1)).getAllUsers();
        verify(scanner, never()).nextLine();
    }

    @Test
    void testBlockUser_InvalidChoice() {
        User user1 = new User("User1", "user1@example.com", "password1");
        User user2 = new User("User2", "user2@example.com", "password2");
        Map<String, User> users = new HashMap<>();
        users.put(user1.getEmail(), user1);
        users.put(user2.getEmail(), user2);
        when(userService.getAllUsers()).thenReturn(users);
        when(scanner.nextLine()).thenReturn("3", "0");

        adminService.blockUser(scanner);

        assertFalse(user1.isBlocked());
        assertFalse(user2.isBlocked());
        verify(userService, times(1)).getAllUsers();
        verify(scanner, times(2)).nextLine();
    }


    @Test
    void testDeleteUser_NoActiveUsers() {
        when(userService.getAllUsers()).thenReturn(new HashMap<>());

        adminService.deleteUser(scanner);

        verify(userService, times(1)).getAllUsers();
        verify(scanner, never()).nextLine();
    }

    @Test
    void testDeleteUser_InvalidChoice() {
        User user1 = new User("User1", "user1@example.com", "password1");
        User user2 = new User("User2", "user2@example.com", "password2");
        Map<String, User> users = new HashMap<>();
        users.put(user1.getEmail(), user1);
        users.put(user2.getEmail(), user2);
        when(userService.getAllUsers()).thenReturn(users);
        when(scanner.nextLine()).thenReturn("3", "0");

        adminService.deleteUser(scanner);

        verify(userService, times(1)).getAllUsers();
        verify(userService, never()).deleteUser(anyString());
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void testGetAllUsersWithHabits_Success() {
        User user1 = new User("User1", "user1@example.com", "password1");
        User user2 = new User("User2", "user2@example.com", "password2");
        Map<String, User> users = new HashMap<>();
        users.put(user1.getEmail(), user1);
        users.put(user2.getEmail(), user2);
        when(userService.getAllUsers()).thenReturn(users);

        String result = adminService.getAllUsersWithHabits();

        assertNotNull(result);
        assertTrue(result.contains("User: User1"));
        assertTrue(result.contains("User: User2"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsersWithHabits_NoActiveUsers() {
        when(userService.getAllUsers()).thenReturn(new HashMap<>());

        String result = adminService.getAllUsersWithHabits();

        assertEquals("No active users.", result);
        verify(userService, times(1)).getAllUsers();
    }
}