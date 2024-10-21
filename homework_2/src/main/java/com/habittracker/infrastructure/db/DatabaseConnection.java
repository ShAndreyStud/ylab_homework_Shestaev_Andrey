package com.habittracker.infrastructure.db;

import com.habittracker.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {
    private final Connection connection;

    public DatabaseConnection(DatabaseConfig config) throws SQLException {
        this.connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        connection.setAutoCommit(false);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}