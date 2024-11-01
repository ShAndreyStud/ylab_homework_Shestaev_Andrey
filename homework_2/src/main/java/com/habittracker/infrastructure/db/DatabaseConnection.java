package com.habittracker.infrastructure.db;

import com.habittracker.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, отвечающий за установку и управление соединением с базой данных.
 * Реализует {@link AutoCloseable}, что позволяет безопасно закрывать соединение
 * после завершения работы.
 */
public class DatabaseConnection implements AutoCloseable {
    /** Соединение с базой данных. */
    private final Connection connection;

    /**
     * Конструктор класса DatabaseConnection, создает соединение с базой данных
     * с использованием параметров, указанных в конфигурации.
     *
     * @param config конфигурация базы данных, содержащая URL, имя пользователя и пароль
     * @throws SQLException если возникает ошибка при попытке подключения к базе данных
     */
    public DatabaseConnection(DatabaseConfig config) throws SQLException {
        this.connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        connection.setAutoCommit(false);
    }

    /**
     * Получает текущее соединение с базой данных.
     *
     * @return объект {@link Connection}, представляющий соединение с базой данных
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Закрывает текущее соединение с базой данных, если оно открыто.
     *
     * @throws SQLException если возникает ошибка при закрытии соединения
     */
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}