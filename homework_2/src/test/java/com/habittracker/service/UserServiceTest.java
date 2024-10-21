package com.habittracker.service;

import com.habittracker.model.User;
import com.habittracker.repository.UserRepository;
import com.habittracker.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервиса пользователя")
class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private Connection mockConnection;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        mockConnection = Mockito.mock(Connection.class);
        userService = new UserService(userRepository);

    }

    @Test
    @DisplayName("Проверка успешной регистрации пользователя")
    public void testRegisterUser_Success() {

        when(userRepository.getUser("test@example.com")).thenReturn(null);

        String result = userService.registerUser("Test User", "test@example.com", "password123");

        assertEquals("The registration was successful.", result);

        verify(userRepository, times(1)).addUser(any(User.class));
    }

    @Test
    @DisplayName("Проверка вывода ошибки при занятой почте")
    public void testRegisterUser_EmailAlreadyExists() {

        User existingUser = new User("Existing User", "test@example.com", "password123", User.Role.USER);
        when(userRepository.getUser("test@example.com")).thenReturn(existingUser);

        String result = userService.registerUser("Test User", "test@example.com", "password123");

        assertEquals("Error: A user with this email address is already registered.", result);

        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    @DisplayName("Проверка успешной авторизации пользователя")
    public void testLoginUser_Success() {
        User existingUser = new User("Test User", "test@example.com", "password123", User.Role.USER);
        when(userRepository.getUser("test@example.com")).thenReturn(existingUser);

        Optional<User> result = userService.loginUser("test@example.com", "password123");

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
    }

    @Test
    @DisplayName("Проверка ошибки авторизации")
    public void testLoginUser_WrongPassword() {
        User existingUser = new User("Test User", "test@example.com", "password123", User.Role.USER);
        when(userRepository.getUser("test@example.com")).thenReturn(existingUser);

        Optional<User> result = userService.loginUser("test@example.com", "wrongPassword");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка зарегистрированного отсутсвия пользователя")
    public void testLoginUser_UserNotFound() {
        when(userRepository.getUser("nonexistent@example.com")).thenReturn(null);

        Optional<User> result = userService.loginUser("nonexistent@example.com", "password123");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка успешного обновления данных пользователя")
    public void testUpdateUserProfile_Success() {
        User existingUser = new User("John Doe", "john@example.com", "password123", User.Role.USER);
        when(userRepository.getUser("newemail@example.com")).thenReturn(null);

        Optional<User> result = userService.updateUserProfile(existingUser, "John Smith", "newemail@example.com");

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());

        verify(userRepository).updateUser(existingUser, "John Smith", "newemail@example.com");
    }

    @Test
    @DisplayName("Проверка ошибки обновления данных пользователя при занятой почте")
    public void testUpdateUserProfile_EmailAlreadyExists() {
        User existingUser = new User("John Doe", "john@example.com", "password123", User.Role.USER);
        User otherUser = new User("Jane Smith", "newemail@example.com", "password456", User.Role.USER);
        when(userRepository.getUser("newemail@example.com")).thenReturn(otherUser);

        Optional<User> result = userService.updateUserProfile(existingUser, "John Smith", "newemail@example.com");

        assertFalse(result.isPresent());

        verify(userRepository, never()).updateUser(any(), any(), any());
    }

    @Test
    @DisplayName("Проверка обновления данных с той же почтой")
    public void testUpdateUserProfile_NoChangeInEmail() {
        User existingUser = new User("John Doe", "john@example.com", "password123", User.Role.USER);
        when(userRepository.getUser("john@example.com")).thenReturn(existingUser);

        Optional<User> result = userService.updateUserProfile(existingUser, "John Smith", "john@example.com");

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());

        verify(userRepository).updateUser(existingUser, "John Smith", "john@example.com");
    }

    @Test
    @DisplayName("Проверка блокировки пользователя")
    public void testUpdateUserProfile_BlockUser() {
        User user = new User("John Doe", "john@example.com", "password123", User.Role.USER);

        Optional<User> result = userService.updateUserProfile(user, true);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).blockUser(user, true);
    }

    @Test
    @DisplayName("Проверка разблокировки пользователя")
    public void testUpdateUserProfile_UnblockUser() {
        User user = new User("John Doe", "john@example.com", "password123", User.Role.USER);

        Optional<User> result = userService.updateUserProfile(user, false);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).blockUser(user, false);
    }

    @Test
    @DisplayName("Проверка успешного удаления профиля пользователя")
    public void testDeleteUser_Success() {
        User user = new User("John Doe", "john@example.com", "password123", User.Role.USER);

        userService.deleteUser(user);

        verify(userRepository).deleteUser(user);
    }

    @Test
    @DisplayName("Проверка успешной смены пароля")
    public void testResetPassword_Success() {
        User user = new User("John Doe", "john@example.com", "oldPassword", User.Role.USER);
        String newPassword = "newPassword123";

        when(userRepository.updateUserPassword(user, newPassword)).thenReturn(user);

        User updatedUser = userService.resetPassword(user, newPassword);

        assertEquals(user, updatedUser);
        verify(userRepository).updateUserPassword(user, newPassword);
    }

    @Test
    @DisplayName("Проверка успешного получения списка пользователей")
    public void testGetAllUsers() {
        User admin = new User("Admin", "admin", "admin", User.Role.ADMIN);
        User user1 = new User("User1", "user1@example.com", "password1", User.Role.USER);
        User user2 = new User("User2", "user2@example.com", "password2", User.Role.USER);

        List<User> expectedUsers = Arrays.asList(admin, user1, user2);

        when(userRepository.getAllUsers()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).getAllUsers();
    }

}
