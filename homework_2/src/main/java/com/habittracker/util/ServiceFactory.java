package com.habittracker.util;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.controller.MenuController;
import com.habittracker.repository.*;
import com.habittracker.service.HabitCompletionService;
import com.habittracker.service.HabitService;
import com.habittracker.service.UserService;

import java.sql.Connection;

public class ServiceFactory {
    private final DatabaseConfig config;
    private UserRepository userRepository;
    private HabitRepository habitRepository;
    private HabitCompletionRepository habitCompletionRepository;
    private UserService userService;
    private HabitService habitService;
    private HabitCompletionService habitCompletionService;

    public ServiceFactory(DatabaseConfig config) {
        this.config = config;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl(config);
        }
        return userRepository;
    }

    public HabitRepository getHabitRepository() {
        if (habitRepository == null) {
            habitRepository = new HabitRepositoryImpl(config);
        }
        return habitRepository;
    }

    public HabitCompletionRepository getHabitCompletionRepository() {
        if (habitCompletionRepository == null) {
            habitCompletionRepository = new HabitCompletionRepositoryImpl(config);
        }
        return habitCompletionRepository;
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = new UserService(getUserRepository());
        }
        return userService;
    }

    public HabitService getHabitService() {
        if (habitService == null) {
            habitService = new HabitService(getHabitCompletionRepository(), getHabitRepository());
        }
        return habitService;
    }

    public HabitCompletionService getHabitCompletionService() {
        if (habitCompletionService == null) {
            habitCompletionService = new HabitCompletionService(getHabitCompletionRepository());
        }
        return habitCompletionService;
    }

    public MenuController getMenuController() {
        return new MenuController(getUserService(), getHabitService(), getHabitCompletionService());
    }
}
