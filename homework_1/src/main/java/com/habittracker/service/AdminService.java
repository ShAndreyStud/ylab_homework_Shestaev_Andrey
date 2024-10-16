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

    // Блокировка пользователя
    public void blockUser(Scanner scanner) {
        Map<String, User> users = userService.getAllUsers();
        List<User> usersList = users.values().stream().toList();
        if(usersList.isEmpty()){
            System.out.println("No active users.");
            return;
        }
        for (int i = 0; i < usersList.size(); i++) {
            System.out.println(i+1 + ". " + usersList.get(i).getName());
        }
        System.out.println("Select user to block, or type 0 to exit:");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > usersList.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(choice == 0) {
            return;
        } else {
            usersList.get(choice-1).setBlocked(true);
            System.out.println("User has been blocked.");
        }
    }

    // Удаление пользователя
    public void deleteUser(Scanner scanner) {
        Map<String, User> users = userService.getAllUsers();
        List<User> usersList = users.values().stream().toList();
        if(usersList.isEmpty()){
            System.out.println("No active users.");
            return;
        }
        for (int i = 0; i < usersList.size(); i++) {
            System.out.println(i+1 + ". " + usersList.get(i).getName());
        }
        System.out.println("Select user to delete, or type 0 to exit:");
        int choice;
        while(true){
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 0 || choice > usersList.size()) {
                    System.out.println("Wrong habit number. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter right number.");
            }
        }
        if(choice == 0) {
            return;
        } else {
            userService.deleteUser(usersList.get(choice-1).getEmail());
            System.out.println("User has been blocked.");
        }
    }

    // Получение списка пользователей и их привычек
    public String getAllUsersWithHabits() {
        Map<String, User> users = userService.getAllUsers(); // Нужно добавить метод в UserService
        StringBuilder result = new StringBuilder();

        for (User user : users.values()) {
            if(!user.isAdmin()) {
                result.append("User: ").append(user.getName()).append("\n");
                result.append("Habits: \n");
                user.getHabits().forEach(habit -> result.append(" - ").append(habit.getName()).append("\n"));
            }
        }

        if(result.isEmpty()) {
            return "No active users.";
        } else {
            return result.toString();
        }
    }
}
