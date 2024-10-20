package com.habittracker.repository;

import com.habittracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
        testUser = new User("Test User", "test@example.com", "password", User.Role.USER);
        userRepository.addUser(testUser);
    }

    @Test
    void testGetUser_Success() {
        User result = userRepository.getUser("test@example.com");

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void testGetUser_UserNotFound() {
        User result = userRepository.getUser("nonexistent@example.com");

        assertNull(result);
    }

    @Test
    void testGetUser_NullEmail() {
        User result = userRepository.getUser(null);

        assertNull(result);
    }

    @Test
    void testAddUser_Success() {
        User testUser2 = new User("Test User2", "test2@example.com", "password", User.Role.USER);
        boolean result = userRepository.addUser(testUser2);

        assertTrue(result);
        assertEquals(testUser, userRepository.getUser(testUser.getEmail()));
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        userRepository.addUser(testUser);

        boolean result = userRepository.addUser(testUser);

        assertFalse(result);
    }

    @Test
    void testAddUser_NullUser() {
        boolean result = userRepository.addUser(null);

        assertFalse(result);
    }

    @Test
    void testAddUser_UserWithSameEmail() {
        userRepository.addUser(testUser);

        User newUser = new User("Another User", "test@example.com", "newpassword", User.Role.ADMIN);

        boolean result = userRepository.addUser(newUser);

        assertFalse(result);
        assertEquals(testUser, userRepository.getUser(testUser.getEmail()));
    }

    @Test
    void testUpdateUser_Success() {
        String newName = "Updated User";
        String newEmail = "updated@example.com";

        boolean result = userRepository.updateUser(testUser, newName, newEmail);

        assertTrue(result);
        User updatedUser = userRepository.getUser(newEmail);
        assertNotNull(updatedUser);
        assertEquals(newName, updatedUser.getName());
        assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        User nonExistentUser = new User("Non Existent", "nonexistent@example.com", "password", User.Role.USER);

        boolean result = userRepository.updateUser(nonExistentUser, "New Name", "newemail@example.com");

        assertFalse(result);
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        User anotherUser = new User("Another User", "another@example.com", "password", User.Role.ADMIN);
        userRepository.addUser(anotherUser);

        boolean result = userRepository.updateUser(testUser, "Updated User", "another@example.com");

        assertFalse(result);
        User unchangedUser = userRepository.getUser(testUser.getEmail());
        assertNotNull(unchangedUser);
        assertEquals(testUser.getName(), unchangedUser.getName());
        assertEquals(testUser.getEmail(), unchangedUser.getEmail());
    }

    @Test
    void testUpdateUser_SameEmail() {
        boolean result = userRepository.updateUser(testUser, "Updated User", testUser.getEmail());

        assertTrue(result);
        User updatedUser = userRepository.getUser(testUser.getEmail());
        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
        assertEquals(testUser.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testUpdateUser_InvalidEmail() {
        boolean result = userRepository.updateUser(testUser, "Updated User", "");

        assertFalse(result);
        User unchangedUser = userRepository.getUser(testUser.getEmail());
        assertNotNull(unchangedUser);
        assertEquals(testUser.getName(), unchangedUser.getName());
        assertEquals(testUser.getEmail(), unchangedUser.getEmail());
    }

    @Test
    void testBlockUser_Success() {
        boolean result = userRepository.blockUser(testUser, true);

        assertTrue(result);
        User blockedUser = userRepository.getUser(testUser.getEmail());
        assertNotNull(blockedUser);
        assertTrue(blockedUser.isBlocked());
    }

    @Test
    void testBlockUser_UserDoesNotExist() {
        User nonExistentUser = new User("Non Existent", "nonexistent@example.com", "password", User.Role.USER);

        boolean result = userRepository.blockUser(nonExistentUser, true);

        assertFalse(result);
    }

    @Test
    void testDeleteUser_Success() {
        boolean result = userRepository.deleteUser(testUser);

        assertTrue(result);
        User deletedUser = userRepository.getUser(testUser.getEmail());
        assertNull(deletedUser);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        User nonExistentUser = new User("Non Existent", "nonexistent@example.com", "password", User.Role.USER);

        boolean result = userRepository.deleteUser(nonExistentUser);

        assertFalse(result);
    }

    @Test
    void testUpdateUserPassword_Success() {
        String newPassword = "newPassword123";

        User updatedUser = userRepository.updateUserPassword(testUser, newPassword);

        assertEquals(newPassword, updatedUser.getPassword());

        User retrievedUser = userRepository.getUser(testUser.getEmail());
        assertEquals(newPassword, retrievedUser.getPassword());
    }

    @Test
    void testUpdateUserPassword_UserDoesNotExist() {
        User nonExistentUser = new User("Non Existent", "nonexistent@example.com", "password", User.Role.USER);

        assertThrows(NullPointerException.class, () -> {
            userRepository.updateUserPassword(nonExistentUser, "newPassword123");
        });
    }

    @Test
    void testGetAllUsers_EmptyRepository() {
        List<User> users = userRepository.getAllUsers();
        users.remove(testUser);

        assertTrue(users.isEmpty());
    }

    @Test
    void testGetAllUsers_OnlyUsers() {
        User user1 = new User("User One", "user1@example.com", "password", User.Role.USER);
        User user2 = new User("User Two", "user2@example.com", "password", User.Role.USER);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        List<User> users = userRepository.getAllUsers();

        assertEquals(3, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testGetAllUsers_WithAdmin() {
        User user1 = new User("User One", "user1@example.com", "password", User.Role.USER);
        User admin = new User("Admin", "admin@example.com", "adminpassword", User.Role.ADMIN);
        userRepository.addUser(user1);
        userRepository.addUser(admin);

        List<User> users = userRepository.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertFalse(users.contains(admin));
    }

}
