package com.habittracker;

import com.habittracker.controller.MenuController;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.HabitRepositoryImpl;
import com.habittracker.service.HabitCompletionService;
import com.habittracker.service.HabitService;
import com.habittracker.service.UserService;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        HabitCompletionService habitCompletionService = new HabitCompletionService();
        HabitRepository habitRepository = new HabitRepositoryImpl();
        HabitService habitService = new HabitService(habitCompletionService.habitCompletionRepository, habitRepository);
        MenuController menuController = new MenuController(userService, habitService, habitCompletionService);
        menuController.start();
    }
}
