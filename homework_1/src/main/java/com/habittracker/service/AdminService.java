package com.habittracker.service;

import com.habittracker.model.User;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AdminService {
    private UserService userService;

    public AdminService(UserService userService) {
        this.userService = userService;
    }

    // Получение списка пользователей и их привычек
    public List<User> getAllUsersWithHabits() {
        Map<String, User> users = userService.getAllUsers();
        List<User> usersList = users.values().stream().toList();
        return usersList;
    }
}
