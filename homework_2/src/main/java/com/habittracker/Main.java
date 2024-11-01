package com.habittracker;

import com.habittracker.config.DatabaseConfig;
import com.habittracker.controller.MenuController;
import com.habittracker.infrastructure.db.DatabaseConnection;
import com.habittracker.infrastructure.db.migration.LiquibaseMigration;
import com.habittracker.util.ServiceFactory;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig("homework_2/src/main/resources/application.properties");

        try (DatabaseConnection dbConnection = new DatabaseConnection(config)) {
            Connection connection = dbConnection.getConnection();


            LiquibaseMigration migration = new LiquibaseMigration(connection);
            migration.runMigrations();
            connection.close();

            ServiceFactory factory = new ServiceFactory(config);
            MenuController menuController = factory.getMenuController();

            menuController.start();
        } catch (Exception e) {
            System.err.println("Migration or database setup failed: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
