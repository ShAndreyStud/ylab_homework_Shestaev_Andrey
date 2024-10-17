package com.habittracker.controller;

import com.habittracker.model.User;


public class MenuController {

    private User currentUser; // Текущий пользователь

    public void start() {
        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else if (currentUser.getRole() == User.Role.USER) {
                showUserMenu();
            } else if (currentUser.getRole() == User.Role.ADMIN) {
                showAdminMenu();
            }
        }
    }

    private void showGuestMenu() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        // Обработка ввода
    }

    private void showUserMenu() {
        System.out.println("1. View Habits");
        System.out.println("2. Mark Habit as Completed");
        // Обработка ввода
    }

    private void showAdminMenu() {
        System.out.println("1. Manage Users");
        System.out.println("2. View All Habits");
        // Обработка ввода
    }

}
