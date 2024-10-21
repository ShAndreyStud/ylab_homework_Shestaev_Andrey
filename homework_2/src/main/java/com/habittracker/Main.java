package com.habittracker;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.controller.MenuController;
import com.habittracker.infrastructure.db.DatabaseConnection;
import com.habittracker.infrastructure.db.migration.LiquibaseMigration;
import com.habittracker.repository.*;
import com.habittracker.service.HabitCompletionService;
import com.habittracker.service.HabitService;
import com.habittracker.service.UserService;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        try (DatabaseConnection dbConnection = new DatabaseConnection(new DatabaseConfig("homework_2/src/main/resources/application.properties"))) {
            Connection connection = dbConnection.getConnection();

            LiquibaseMigration migration = new LiquibaseMigration(connection);
            migration.runMigrations();


            UserRepository userRepository = new UserRepositoryImpl(connection);
            UserService userService = new UserService(userRepository);
            HabitRepository habitRepository = new HabitRepositoryImpl(connection);
            HabitCompletionRepository habitCompletionRepository = new HabitCompletionRepositoryImpl(connection);
            HabitCompletionService habitCompletionService = new HabitCompletionService(habitCompletionRepository);
            HabitService habitService = new HabitService(habitCompletionRepository, habitRepository);

            MenuController menuController = new MenuController(userService, habitService, habitCompletionService);
            menuController.start();

        } catch (Exception e) {
            System.err.println("Migration or database setup failed: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
